package com.example.admin.vktargetapp.com.example.admin.vktargetapp.models;

public class FinishedTask extends Task {
    public String FinishedDate;
    public FinishedTask(String description, String siteIcon, double price, String linkUrl, String linkText, String finishedDate){
        super(description, siteIcon, price, linkUrl, linkText);
        FinishedDate = finishedDate;
    }
}
