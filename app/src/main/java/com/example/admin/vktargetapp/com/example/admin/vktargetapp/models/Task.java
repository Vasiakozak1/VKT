package com.example.admin.vktargetapp.com.example.admin.vktargetapp.models;

public class Task {
    public String Description;
    public int SiteIconResourceId;
    public double Price;
    public String LinkUrl;
    public String LinkText;

    public Task(){
    }

    public Task(String description, int siteIconResourceId, double price, String linkUrl, String linkText) {
        Description = description;
        SiteIconResourceId = siteIconResourceId;
        Price = price;
        LinkUrl = linkUrl;
        LinkText =linkText;
    }
}
