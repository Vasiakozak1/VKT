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

    public void setEmailAndPassword(String email, String password) {
        sharedPreferences.edit().putString("email", email).commit();
        sharedPreferences.edit().putString("password", password).commit();
    }
    public String getCurrentEmail() {
        return sharedPreferences.getString("email", "");
    }
    public String getCurrentPassword() {
        return sharedPreferences.getString("password", "");
    }
}