package com.example.admin.vktargetapp;

import android.app.AlertDialog;
import android.content.Context;
import android.webkit.JavascriptInterface;

public class VkTargetJSInterface {
    private Context context;

    public  VkTargetJSInterface(Context context){
        this.context = context;
    }

    @JavascriptInterface
    public void getApiKey(String apiKey){
        if(apiKey == null || apiKey.isEmpty()) {
            return;
        }

        new AlertDialog.Builder(this.context).setTitle("api key").setMessage(apiKey)
                .setPositiveButton(android.R.string.ok, null)
                .setCancelable(false).create().show();
    }
}
