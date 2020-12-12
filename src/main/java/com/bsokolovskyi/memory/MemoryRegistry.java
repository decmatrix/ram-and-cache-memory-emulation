package com.bsokolovskyi.memory;

import com.bsokolovskyi.program.Program;

import java.util.LinkedList;
import java.util.List;

public class MemoryRegistry {
    private final List<MemoryContainer> registry;

    public MemoryRegistry() {
        this.registry = new LinkedList<>();
    }

    public void put(Program program, MemorySlice slice) {
        MemoryContainer container = new MemoryContainer(program, slice);

        if(contains(program.getId())) {
            return;
        }

        program.programLoadedToMemory(true);
        registry.add(container);
    }

    public void remove(long id) {
        if(!contains(id)) {
            return;
        }

        MemoryContainer container = get(id);
        container.getProgram().programLoadedToMemory(false);
        registry.remove(container);
    }

    public MemoryContainer get(long id) {
        for(MemoryContainer container : registry) {
            if(container.getProgram().getId() == id) {
                return container;
            }
        }

        return null;
    }

    public void clear() {
        registry.clear();
    }

    public boolean contains(long id) {
        for(MemoryContainer container : registry) {
            if(container.getProgram().getId() == id) {
                return true;
            }
        }

        return false;
    }

    public static class MemoryContainer {
        private final MemorySlice slice;
        private final Program program;

        private MemoryContainer(Program program, MemorySlice slice) {
            this.program = program;
            this.slice = slice;
        }

        public Program getProgram() {
            return program;
        }

        public MemorySlice getSlice() {
            return slice;
        }
    }
}
