package com.example.admin.vktargetapp.com.example.admin.vktargetapp.models;

import android.webkit.WebView;
import android.widget.Button;

public class TaskData {
    public String email;
    public WebView webView;
    public Button checkTaskButton;

    public TaskData(String email, WebView webView) {
        this.email = email;
        this.webView = webView;
    }
}
