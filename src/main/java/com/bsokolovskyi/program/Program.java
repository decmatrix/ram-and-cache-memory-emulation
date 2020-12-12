package com.bsokolovskyi.program;

import com.bsokolovskyi.memory.MemoryCell;

import java.util.Random;

public class Program {
    private final long id;
    private final int size;
    private String name;
    private boolean loadedToMemory;
    private final MemoryCell[] programMemory;

    private static final Random random = new Random();

    public Program(long id, int size) {
        this(id, size, makeNme(id));
    }

    public Program(long id, int size, String name) {
        this.id = id;
        this.size = size;
        this.name = name;
        this.loadedToMemory = false;

        programMemory = new MemoryCell[size];
        for(int i = 0; i < size; i++) {
            programMemory[i] = new MemoryCell(i);
            programMemory[i].writeData(generateRandomData());
        }
    }

    public long getId() {
        return id;
    }

    public long getSize() {
        return size;
    }

    public void offsetAddresses(int offset) {
        for(int i = 0; i < size; i++) {
            programMemory[i].setAddress(programMemory[i].getAddress() + offset);
        }
    }

    public String getName() {
        return name;
    }

    public MemoryCell[] getProgramMemory() {
        return programMemory;
    }

    public char[] getData() {
        char[] data = new char[size];

        for(int i = 0; i < size; i++) {
            data[i] = programMemory[i].readData();
        }

        return data;
    }

    public void programLoadedToMemory(boolean loadedToMemory) {
        this.loadedToMemory = loadedToMemory;
    }

    public void renameProgram(String newName) {
        this.name = newName;
    }

    public static String makeNme(long id) {
        return String.format("%d-program", id);
    }

    @Override
    public String toString() {
        return String.format("id - %d, size - %d c.u., name - %s, loaded to mem - %s", id, size, name, loadedToMemory);
    }

    private char generateRandomData() {
        char data = '.';

        while(data == '.' || Character.isWhitespace(data)) {
            data = (char) (random.nextInt(95) + 32);
        }

        return data;
    }
}
