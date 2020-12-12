package com.bsokolovskyi.memory;

import java.util.LinkedList;
import java.util.List;

public class RAMMemory extends Memory {
    private final List<MemorySlice> usedMemoryTable;
    private final MemorySlice RAM;

    public RAMMemory(int size) {
        super(size);

        this.RAM = new MemorySlice(0, size - 1);
        this.usedMemoryTable = new LinkedList<>();
    }

    public ReturnedStatus writeData(MemorySlice memorySlice, char[] data, CashMemory cashMemory) {
        if(!canWriteTo(memorySlice)) {
            return ReturnedStatus.MEMORY_SLICE_NO_FREE;
        }

        if(outOfMemory(memorySlice)) {
            return ReturnedStatus.OUT_OF_MEMORY;
        }

        usedMemoryTable.add(memorySlice);
        for(int i = memorySlice.getStartAddress(), j = 0; i <= memorySlice.getEndAddress(); i++, j++) {
            int cashAddr = cashMemory.getAssocCashAddress(i);
            Integer lastRamAddr = cashMemory.getLastUsedAddr(cashAddr);

            if(lastRamAddr != null && i == lastRamAddr) {
                getTable()[lastRamAddr].writeDataUnsafe(cashMemory.readData(cashAddr));
                cashMemory.writeData(cashAddr, i, data[j]);
                getTable()[i].setIsValid();
            } else {
                getTable()[i].writeData(data[j]);
            }
        }

        return ReturnedStatus.WRITE_OK;
    }

    public void readData(MemorySlice slice, CashMemory cashMemory, DataResponse response) {
        if(outOfMemory(slice)) {
            response.addStatus(ReturnedStatus.OUT_OF_MEMORY);
            return;
        }

        StringBuilder readData = new StringBuilder();
        for(int i = slice.getStartAddress(); i <= slice.getEndAddress(); i++) {
            int cashAddr = cashMemory.getAssocCashAddress(i);
            char oldData = cashMemory.readData(cashAddr);
            char dataFromRam = getTable()[i].readData();
            Integer lastRamAddr = cashMemory.getLastUsedAddr(cashAddr);

            if(lastRamAddr != null) {
                if(lastRamAddr == i) {
                    readData.append(oldData);
                } else {
                    cashMemory.writeData(cashAddr, i, dataFromRam);
                    getTable()[lastRamAddr].writeDataUnsafe(oldData);
                    readData.append(dataFromRam);
                }
            } else {
                cashMemory.writeData(cashAddr, i, dataFromRam);
                readData.append(dataFromRam);
            }

        }

        response.addStatus(ReturnedStatus.READ_OK);
        response.setData(readData.toString());
    }

    @Override
    public void freeMemory(MemorySlice slice) {
        usedMemoryTable.remove(slice);
        super.freeMemory(slice);
    }

    public MemoryResponse allocateFreeMemory(long size) {
        MemoryResponse response;

        if(memoryIsFull()) {
            response = new MemoryResponse(null);
            response.addStatus(ReturnedStatus.NO_MEMORY_FREE);
            return response;
        }

        int startAddress = -1;
        int endAddress = -1;

        for(int i = 0; i < getTable().length; i++) {
            if(!getTable()[i].hasValid() && startAddress == -1) {
                startAddress = endAddress = i;
            }

            if(!getTable()[i].hasValid() && startAddress != -1) {
                endAddress = i;
            }

            if((endAddress - startAddress + 1) == size) {
                response = new MemoryResponse(new MemorySlice(startAddress, endAddress));
                response.addStatus(ReturnedStatus.MEMORY_ALLOCATED);
                return response;
            }

            if(getTable()[i].hasValid()) {
                startAddress = endAddress = -1;
            }
        }

        response = new MemoryResponse(null);
        response.addStatus(ReturnedStatus.NO_MEMORY_FREE);
        return response;
    }

    @Override
    public void fullFreeMemory() {
        super.fullFreeMemory();
        usedMemoryTable.clear();
    }

    private boolean outOfMemory(MemorySlice slice) {
        return !RAM.subIn(slice.getStartAddress(), slice.getEndAddress());
    }

    private boolean canWriteTo(MemorySlice slice) {
        return !usedMemoryTable.contains(slice);
    }
}
