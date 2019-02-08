package com.example.admin.vktargetapp;

import android.widget.Button;

import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.TaskData;

import java.util.HashMap;
import java.util.Map;

public class TaskDataStorage {
    private static TaskDataStorage instance;
    private Map<String, TaskData> webViews;
    private Map<String, Button> checkTaskButtons;
    private TaskDataStorage() {
        webViews = new HashMap<>();
        checkTaskButtons = new HashMap<>();
    }

    public TaskData getWebView(String taskLinkUrl, int taskIcon) {
        return webViews.get(taskLinkUrl + taskIcon);
    }

    public void addWebView(String taskLinkUrl, int taskIcon, TaskData webView) {
        webViews.put(taskLinkUrl + taskIcon, webView);
    }

    public Button getCheckTaskButton(String taskLinkUrl, int taskIcon) {
        return checkTaskButtons.get(taskLinkUrl + taskIcon);
    }

    public void addCheckTaskButton(String taskLinkUrl, int taskIcon, Button button) {
        checkTaskButtons.put(taskLinkUrl + taskIcon, button);
    }

    public static TaskDataStorage getInstance() {
        if(instance == null) {
            instance = new TaskDataStorage();
        }
        return instance;
    }
}
