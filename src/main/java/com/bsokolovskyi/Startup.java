package com.bsokolovskyi;

import com.bsokolovskyi.memory.CashMemory;
import com.bsokolovskyi.memory.MemoryManager;
import com.bsokolovskyi.memory.RAMMemory;
import com.bsokolovskyi.controllers.Manager;

public class Startup {
    public static void main(String[] args) {
        RAMMemory ramMemory = new RAMMemory(8);
        CashMemory cashMemory = new CashMemory(ramMemory);
        MemoryManager manager = new MemoryManager(ramMemory, cashMemory);

        Manager.create(ramMemory, cashMemory, manager).run();
    }
}
