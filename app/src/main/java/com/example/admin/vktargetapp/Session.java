package com.example.admin.vktargetapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences sharedPreferences;
    public static NeedsLogin NeedsLogin = com.example.admin.vktargetapp.NeedsLogin.CheckIsNeeded;
    public Session(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString("email", email).commit();
    }
    public String getCurrentEmail() {
        return sharedPreferences.getString("email", "");
    }
}