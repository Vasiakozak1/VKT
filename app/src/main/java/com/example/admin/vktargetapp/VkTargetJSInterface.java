package com.example.admin.vktargetapp;

import android.app.AlertDialog;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class VkTargetJSInterface {
    private Context context;

    public  VkTargetJSInterface(Context context){
        this.context = context;
    }

    @JavascriptInterface
    public void setApiKey(String apiKey){
        if(apiKey == null || apiKey.isEmpty()) {
            return;
        }

        new AlertDialog.Builder(this.context).setTitle("api key").setMessage(apiKey)
                .setPositiveButton(android.R.string.ok, null)
                .setCancelable(false).create().show();
    }
}
