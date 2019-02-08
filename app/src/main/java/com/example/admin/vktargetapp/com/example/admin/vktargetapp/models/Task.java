package com.example.admin.vktargetapp.com.example.admin.vktargetapp.models;

import android.webkit.WebView;

import java.util.HashMap;
import java.util.Map;

public class Task {
    public String Description;
    public int SiteIconResourceId;
    public double Price;
    public String LinkUrl;
    public String LinkText;
    public int Type;

    public Task(){
    }

    public Task(String description, int siteIconResourceId, double price, String linkUrl, String linkText) {
        Description = description;
        SiteIconResourceId = siteIconResourceId;
        Price = price;
        LinkUrl = linkUrl;
        LinkText = linkText;
    }
}
