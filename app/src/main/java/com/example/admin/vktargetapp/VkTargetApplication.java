package com.example.admin.vktargetapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;

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

    public static String getApiKey() {
        SharedPreferences preferences = VkTargetApplication
                .getCurrentActivity()
                .getSharedPreferences(Constants.PREFERENCES_NAME, MODE_PRIVATE);
        return preferences.getString("apiKey", "");
    }

    public static void setApiKey(String apiKey) {
        SharedPreferences.Editor preferences = VkTargetApplication
                .getCurrentActivity()
                .getSharedPreferences(Constants.PREFERENCES_NAME, MODE_PRIVATE)
                .edit();
        preferences.putString("apiKey", apiKey);
        preferences.commit();
    }

    public static void setLoading() {
        getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.ProgressBar.setVisibility(View.VISIBLE);
            }
        });

    }
    public static void setLoaded() {
        getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.ProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public static void enableNavigationMenu() {
        Menu menu = MainActivity.AppNavigationView.getMenu();
        for(int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
            menu.getItem(menuItemIndex).setEnabled(true);
        }
    }
    public static void disableNavigationMenu() {
        Menu menu = MainActivity.AppNavigationView.getMenu();
        for(int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
            menu.getItem(menuItemIndex).setEnabled(false);
        }
    }

    public static Activity getCurrentActivity() {
        return VkTargetApplication.currentActivity;
    }
    public static Fragment getCurrentFragment(){ return VkTargetApplication.currentFragment; }
    public static NavigationHost getNavigationHost(){ return (NavigationHost) VkTargetApplication.currentActivity; }
}
