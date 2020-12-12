package com.bsokolovskyi.memory;

import java.util.ArrayList;
import java.util.List;

public class DataResponse {
    private String data;
    private final List<ReturnedStatus> statusList;

    public DataResponse() {
        this.data = "";
        this.statusList = new ArrayList<>();
    }

    public void addStatus(ReturnedStatus status) {
        statusList.add(status);
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public ReturnedStatus getLastStatus() {
        return statusList.get(statusList.size() - 1);
    }

    public List<ReturnedStatus> getStatusList() {
        return statusList;
    }
}
