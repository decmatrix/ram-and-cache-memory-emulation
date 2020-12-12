package com.bsokolovskyi.program;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProgramRegistry {
    private final Map<Long, Program> register;
    private static long lastId = 0;

    public ProgramRegistry() {
        this.register = new HashMap<>();
    }

    public void remove(long id) {
        if(!register.containsKey(id)) {
            return;
        }

        register.remove(id);
    }

    public Program put(String name, int size) {
        long id = ++lastId;
        Program program = new Program(id, size, name);
        register.put(id, program);

        return program;
    }

    public void clear() {
        register.clear();
    }

    public Program get(long id) {
        return register.get(id);
    }

    public Collection<Program> getPrograms() {
        return register.values();
    }
}
