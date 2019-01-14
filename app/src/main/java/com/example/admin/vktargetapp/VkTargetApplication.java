package com.example.admin.vktargetapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

public class VkTargetApplication extends Application {
    private static Context context;
    private static Activity currentActivity;
    private static Fragment currentFragment;

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

    public static void setCurrentFragment(Fragment fragment) {
        VkTargetApplication.currentFragment = fragment;
    }

    public static Activity getCurrentActivity() {
        return VkTargetApplication.currentActivity;
    }
    public static Fragment getCurrentFragment(){ return VkTargetApplication.currentFragment; }
    public static NavigationHost getNavigationHost(){ return (NavigationHost) VkTargetApplication.currentActivity; }
}
