package com.example.admin.vktargetapp.com.example.admin.vktargetapp.models;

public class Task {
    public String Description;
    public String SiteIcon;
    public double Price;
    public String LinkUrl;
    public String LinkText;

    public Task(String description, String siteIcon, double price, String linkUrl, String linkText) {
        Description = description;
        SiteIcon = siteIcon;
        Price = price;
        LinkUrl = linkUrl;
        LinkText =linkText;
    }
}
