package com.bsokolovskyi.memory;

public abstract class Memory {
    private final MemoryCell[] table;

    public Memory(int size) {
        if(size <= 0) {
            throw new IllegalArgumentException("Size <= 0 c.u.");
        }

        if(!isPowerOfTwo(size)) {
            throw new IllegalArgumentException("Size isn't power of two");
        }

        this.table = new MemoryCell[size];

        for(int i = 0; i < size; i++) {
            table[i] = new MemoryCell(i);
        }
    }

    public MemoryCell[] getTable() {
        return table;
    }

    public int getSize() {
        return table.length;
    }

    public boolean memoryIsFull() {
        for(MemoryCell cell : table) {
            if(!cell.hasValid()) {
                return false;
            }
        }

        return true;
    }

    public void freeMemory(MemorySlice slice) {
        for(int i = slice.getStartAddress(); i <= slice.getEndAddress(); i++) {
            table[i].free();
        }
    }

    public void fullFreeMemory() {
        for(int i = 0; i < getSize(); i++) {
            table[i].fullFree();
        }
    }

    public static boolean isPowerOfTwo(long size) {
        return (size != 0) && ((size & (size - 1)) == 0);
    }
}
