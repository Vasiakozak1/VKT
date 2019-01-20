package com.example.admin.vktargetapp.com.example.admin.vktargetapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskType {
    @SerializedName("type")
    private int type;

    @SerializedName("price")
    private double price;

    @SerializedName("desc")
    private String description;

    public int getTaskType(){
        return this.type;
    }
    public double getTaskPrice() {
        return this.price;
    }
    public String getTaskDescription() {
        return this.description;
    }

    public void setTaskType(int type) {
        this.type = type;
    }

    public void setTaskPrice(double price) {
        this.price = price;
    }

    public void setTaskDescription(String description) {
        this.description = description;
    }
}
