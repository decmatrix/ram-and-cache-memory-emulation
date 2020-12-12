package com.bsokolovskyi.memory;

public enum ReturnedStatus {
    NO_MEMORY_FREE("no memory free in RAM memory"),
    MEMORY_SLICE_NO_FREE("memory slice no free"),
    WRITE_OK("data successful was written"),
    PROGRAM_NOT_LOADED(" program not loaded"),
    OUT_OF_MEMORY("out of memory"),
    PROGRAM_UNLOADED_AND_MEMORY_FREE("program unloaded and memory free"),
    MEMORY_ALLOCATED("memory allocated"),
    PROGRAM_ALREADY_LOADED("program already loaded"),
    READ_OK("data successful was read");

    private final String info;

    ReturnedStatus(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return info;
    }
}
