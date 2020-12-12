package com.bsokolovskyi.memory;

public class MemorySlice {
    private int startAddress;
    private int endAddress;

    public MemorySlice(int startAddress, int endAddress) {
        this.startAddress = startAddress;
        this.endAddress = endAddress;

        checkAddresses();
    }

    public void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
        checkAddresses();
    }

    public void setEndAddress(int endAddress) {
        this.endAddress = endAddress;
        checkAddresses();
    }

    public int getStartAddress() {
        return startAddress;
    }

    public int getEndAddress() {
        return endAddress;
    }

    public boolean in(int address) {
        return address >= startAddress && address <= endAddress;
    }

    public boolean subIn(int startAddress, int endAddress) {
        return in(startAddress) && in(endAddress);
    }

    private void checkAddresses() {
        if(startAddress < 0 || endAddress < 0) {
            throw new IllegalArgumentException("start address or end address < 0");
        }

        if(endAddress < startAddress) {
            throw new IllegalArgumentException("end address < start address");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MemorySlice)) {
            return false;
        }

        MemorySlice that = (MemorySlice) o;

        return (startAddress == that.startAddress && endAddress == that.endAddress) ||
                (in(that.startAddress) || in(that.endAddress));
    }
}
