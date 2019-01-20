package com.example.admin.vktargetapp.com.example.admin.vktargetapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskTypesResponseObject {
    @SerializedName("response")
    private TaskType[] taskTypes;

    public void setTaskTypes(TaskType[] taskTypeResponse) {
        this.taskTypes = taskTypeResponse;
    }
    public TaskType[] getTaskTypes() {
        return taskTypes;
    }
}
