package com.example.admin.vktargetapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class VkTargetApplication extends Application {
    private static Context context;
    private static Activity currentActivity;

    @Override
    public void onCreate(){
        super.onCreate();
        VkTargetApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return VkTargetApplication.context;
    }

    public static void setCurrentActivity(Activity activity) {
        VkTargetApplication.currentActivity = activity;
    }
    public static Activity getCurrentActivity() {
        return VkTargetApplication.currentActivity;
    }
}
