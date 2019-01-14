package com.example.admin.vktargetapp.com.example.admin.vktargetapp.models;

public class FinishedTask extends Task {
    public String FinishedDate;

    public FinishedTask(){
    }

    public FinishedTask(String description, int siteIconResourceId, double price, String linkUrl, String linkText, String finishedDate){
        super(description, siteIconResourceId, price, linkUrl, linkText);
        FinishedDate = finishedDate;
    }
}
