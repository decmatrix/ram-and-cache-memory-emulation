package com.bsokolovskyi.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashMemory extends Memory {
    private final RAMMemory ramMemory;
    private final Map<Integer, List<Integer>> assocTable;
    private final Map<Integer, Integer> lastUsedTable;
    private final MemorySlice cash;

    public CashMemory(RAMMemory ramMemory) {
        super(ramMemory.getSize() / 2);

        if(getSize() == 1) {
            throw new IllegalArgumentException("Size of cash 1 c.u.");
        }

        this.cash = new MemorySlice(0, getSize() - 1);
        this.ramMemory = ramMemory;
        this.assocTable = new HashMap<>();
        this.lastUsedTable = new HashMap<>();

        initAssocTable();
        establishAssociationBetweenRAMandCash();
    }

    private void initAssocTable() {
        for(int cashAddr = 0; cashAddr < getSize(); cashAddr++) {
            assocTable.put(cashAddr, new ArrayList<Integer>());
        }
    }

    private void establishAssociationBetweenRAMandCash() {
        int cashAddr = 0;
        for (int ramAddr = 0; ramAddr < ramMemory.getSize(); ramAddr++) {
            if(cashAddr == getSize()) {
                cashAddr = 0;
            }

            assocTable.get(cashAddr++).add(ramAddr);
        }
    }

    public boolean hashDataByAddr(int addr) {
        if(outOfMemory(addr)) {
            throw new IllegalArgumentException("Out of cash memory");
        }

        return getTable()[addr].hasData();
    }

    public void writeData(int cashAddr, int ramAddr, char data) {
        if(outOfMemory(cashAddr)) {
            throw new IllegalArgumentException("Out of cash memory");
        }

        lastUsedTable.put(cashAddr, ramAddr);
        getTable()[cashAddr].writeDataUnsafe(data);
    }

    public char readData(int cashAddr) {
        if(outOfMemory(cashAddr)) {
            throw new IllegalArgumentException("Out of cash memory");
        }

        return getTable()[cashAddr].readData();
    }

    public Integer getLastUsedAddr(int cashAddr) {
        if(outOfMemory(cashAddr)) {
            throw new IllegalArgumentException("Out of cash memory");
        }

        if(!lastUsedTable.containsKey(cashAddr)) {
            return null;
        }

        return lastUsedTable.get(cashAddr);
    }

    public int getAssocCashAddress(int ramAddr) {
        for(Map.Entry<Integer, List<Integer>> cashAssocElm : assocTable.entrySet()) {
            if(cashAssocElm.getValue().contains(ramAddr)) {
                return cashAssocElm.getKey();
            }
        }

        throw new IllegalArgumentException("Unknown address 0x" + Integer.toHexString(ramAddr));
    }

    private boolean outOfMemory(int addr) {
        return !cash.in(addr);
    }
}
