package com.bsokolovskyi.memory;

public class MemoryCell {
    private boolean validState;
    private int address;
    private char data;

    public MemoryCell(int address) {
        this.validState = false;
        this.address = address;
        this.data = '.';
    }

    public char readData() {
        return data;
    }

    public boolean hasData() {
        return !(data == '.');
    }

    public void writeData(char data) {
        validState = true;
        this.data = data;
    }

    public void writeDataUnsafe(char data) {
        this.data = data;
    }

    public void free() {
        validState = false;
    }

    public void fullFree() {
        free();
        data = '.';
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setIsValid() {
        validState = true;
    }

    public boolean hasValid() {
        return validState;
    }

    public int getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return String.format("%s(%c)->[%c]",
                "0x" + Integer.toHexString(address),
                validState ? 'X' : 'O',
                data);
    }
}
