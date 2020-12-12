package com.bsokolovskyi.memory;

import com.bsokolovskyi.program.Program;

import java.util.ArrayList;
import java.util.List;

public class MemoryManager {
    private final RAMMemory ramMemory;
    private final CashMemory cashMemory;
    private final MemoryRegistry usedMemoryTable;


    public MemoryManager(RAMMemory ramMemory, CashMemory cashMemory) {
        this.ramMemory = ramMemory;
        this.cashMemory = cashMemory;
        this.usedMemoryTable = new MemoryRegistry();
    }

    public List<ReturnedStatus> loadProgram(Program program) {
        if(!usedMemoryTable.contains(program.getId())) {
        MemoryResponse memoryResponse = ramMemory.allocateFreeMemory(program.getSize());
        ReturnedStatus lastStatus;

            if (memoryResponse.getLastStatus().equals(ReturnedStatus.MEMORY_ALLOCATED)) {
                lastStatus = ramMemory.writeData(memoryResponse.getMemorySlice(), program.getData(), cashMemory);
                memoryResponse.addStatus(lastStatus);

                if (lastStatus.equals(ReturnedStatus.WRITE_OK)) {
                    usedMemoryTable.put(program, memoryResponse.getMemorySlice());

                    //virtual -> physics
                    program.offsetAddresses(memoryResponse.getMemorySlice().getStartAddress());
                }
            }

            return memoryResponse.getStatusList();
        } else {
            List<ReturnedStatus> statusList = new ArrayList<>();
            statusList.add(ReturnedStatus.PROGRAM_ALREADY_LOADED);

            return statusList;
        }
    }

    public List<ReturnedStatus> unloadProgram(long id) {
        List<ReturnedStatus> statusList = new ArrayList<>();

        if(usedMemoryTable.contains(id)) {
            MemoryRegistry.MemoryContainer container = usedMemoryTable.get(id);

            ramMemory.freeMemory(container.getSlice());
            usedMemoryTable.remove(id);

            statusList.add(ReturnedStatus.PROGRAM_UNLOADED_AND_MEMORY_FREE);
        } else {
            statusList.add(ReturnedStatus.PROGRAM_NOT_LOADED);
        }

        return statusList;
    }

    public DataResponse readData(long id) {
        DataResponse response = new DataResponse();

        if(usedMemoryTable.contains(id)) {
            ramMemory.readData(usedMemoryTable.get(id).getSlice(), cashMemory, response);
        } else {
            response.addStatus(ReturnedStatus.PROGRAM_NOT_LOADED);
        }

        return response;
    }

    public void fullReset() {
        ramMemory.fullFreeMemory();
        cashMemory.fullFreeMemory();
        usedMemoryTable.clear();
    }


}
