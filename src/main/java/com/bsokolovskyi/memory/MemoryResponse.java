package com.bsokolovskyi.memory;

import java.util.ArrayList;
import java.util.List;

public class MemoryResponse {
    private final MemorySlice memorySlice;
    private final List<ReturnedStatus> statusList;

    public MemoryResponse(MemorySlice memorySlice) {
        this.memorySlice = memorySlice;
        this.statusList = new ArrayList<>();
    }

    public void addStatus(ReturnedStatus status) {
        statusList.add(status);
    }

    public MemorySlice getMemorySlice() {
        return memorySlice;
    }

    public ReturnedStatus getLastStatus() {
        return statusList.get(statusList.size() - 1);
    }

    public List<ReturnedStatus> getStatusList() {
        return statusList;
    }
}
