package com.bsokolovskyi.controllers;

import com.bsokolovskyi.memory.*;
import com.bsokolovskyi.program.Program;
import com.bsokolovskyi.program.ProgramRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

public class Manager {
    private final RAMMemory ramMemory;
    private final CashMemory cashMemory;
    private final MemoryManager memoryManager;
    private final ProgramRegistry programRegistry;

    private BufferedReader buffReader;

    private static Manager menu;

    public static Manager create(RAMMemory ramMemory, CashMemory cashMemory, MemoryManager manager) {
        if(menu == null) {
            menu = new Manager(ramMemory, cashMemory, manager);
        }

        return menu;
    }

    public void run() {
        buffReader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            int res = runProcessMenu();

            if(res == 0) {
                break;
            } else if(res == -1) {
                System.out.println("Unknown point of menu!");
            } else if(res == -2) {
                System.out.println("Incorrect input");
            }
        }
    }

    private Manager(RAMMemory ramMemory, CashMemory cashMemory, MemoryManager memoryManager) {
        this.ramMemory = ramMemory;
        this.cashMemory = cashMemory;
        this.memoryManager = memoryManager;
        this.programRegistry = new ProgramRegistry();
    }

    private int runProcessMenu() {
        System.out.print("\n" +
                "1. Out RAM memory\n" +
                "2. Out cash memory\n" +
                "3. Load program to registry\n" +
                "4. Unload program from registry\n" +
                "5. Show programs in registry\n" +
                "6. Load program to memory\n" +
                "7. Read data of program\n" +
                "8. Unload program from memory\n" +
                "9. Free all memory\n" +
                "10. Out info about program\n" +
                "11. Exit\n");

        System.out.print("Enter: ");

        try {
            switch (Integer.parseInt(buffReader.readLine())) {
                case 1:
                    outMemory(ramMemory, "RAM");
                    break;
                case 2:
                    outMemory(cashMemory, "cash");
                    break;
                case 3:
                    loadProgramToRegistry();
                    break;
                case 4:
                    unloadProgramFromRegistry();
                    break;
                case 5:
                    outPrograms();
                    break;
                case 6:
                    loadProgramToMemory();
                    break;
                case 7:
                    readDataOfProgram();
                    break;
                case 8:
                    unloadProgramFromMemory();
                    break;
                case 9:
                    freeAllMemory();
                    break;
                case 10:
                    outInfoAboutProgram();
                    break;
                case 11:
                    return 0;
                default:
                    return -1;
            }
        } catch (IOException | NumberFormatException e) {
            return -2;
        }

        return 1;
    }

    private void readDataOfProgram() throws IOException {
        System.out.print("Enter id of program: ");
        DataResponse response = memoryManager.readData(Integer.parseInt(buffReader.readLine()));
        String data = response.getData();

        outStatusList(response.getStatusList());
        System.out.println("\n Data: " + (data.equals("") ? "<absent>" : data));
    }

    private void freeAllMemory() {
        memoryManager.fullReset();
        programRegistry.clear();
        System.out.println("All memory free");
        System.out.println("All programs unloaded from memory");
        System.out.println("All programs unloaded from registry");
    }

    private void outPrograms() {
        System.out.println("Programs in registry:");
        Collection<Program> programs = programRegistry.getPrograms();

        if(programs.isEmpty()) {
            System.out.println(" no programs in the register");
        } else {
            for(Program program : programs) {
                System.out.println("  " + program);
            }
        }
    }

    private void outMemory(Memory memory, String memoryName) {
        System.out.println(" " + memoryName + " memory:");
        for (MemoryCell cell : memory.getTable()) {
            System.out.println(cell);
        }
    }

    private void loadProgramToRegistry() throws IOException, NumberFormatException {
        System.out.print("Enter name of program: ");
        String name = buffReader.readLine();
        System.out.print("Enter size of program: ");
        int size = Integer.parseInt(buffReader.readLine());

        Program program = programRegistry.put(name, size);

        outProgram(program);
    }

    private void outInfoAboutProgram() throws IOException, NumberFormatException {
        System.out.print("Enter id of program: ");
        Program program = getProgramByIdInRegistry(Integer.parseInt(buffReader.readLine()));

        if(program != null) {
            outProgram(program);
        }
    }

    private void unloadProgramFromRegistry() throws IOException, NumberFormatException {
        System.out.print("Enter id of program: ");
        unloadProgramFromRegistry(getProgramByIdInRegistry(Integer.parseInt(buffReader.readLine())));
    }

    private void loadProgramToMemory() throws IOException, NumberFormatException {
        System.out.print("Enter id of program: ");
        Program program = getProgramByIdInRegistry(Integer.parseInt(buffReader.readLine()));

        if(program != null) {
            outStatusList(memoryManager.loadProgram(program));
        }
    }

    private void unloadProgramFromMemory() throws IOException, NumberFormatException {
        System.out.print("Enter id of program: ");
        outStatusList(memoryManager.unloadProgram(Integer.parseInt(buffReader.readLine())));
    }

    private Program getProgramByIdInRegistry(long id) {
        Program program = programRegistry.get(id);

        if(program == null) {
            System.out.println(String.format(" program by id %d not found in registry", id));
        }

        return program;
    }

    private void unloadProgramFromRegistry(Program program) {
        if(program != null) {
            programRegistry.remove(program.getId());
            System.out.println(" program unloaded from registry: " + program);
        }
    }

    private void outStatusList(List<ReturnedStatus> statusList) {
        for(ReturnedStatus status : statusList) {
            System.out.println(" " + status);
        }
    }

    private void outProgram(Program program) {
        System.out.println("Program:");
        System.out.println(" " + program);
        System.out.println("  addresses with data: ");

        for(MemoryCell cell : program.getProgramMemory()) {
            System.out.println("   " + cell);
        }
    }
}
