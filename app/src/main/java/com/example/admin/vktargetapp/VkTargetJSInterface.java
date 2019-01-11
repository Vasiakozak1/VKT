package com.example.admin.vktargetapp;

import android.app.AlertDialog;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class VkTargetJSInterface {
    private final String taskItemClass = "vkt-content__list-item";
    private Context context;
    private NavigationHost navigationHost;

    public  VkTargetJSInterface(Context context){
        this.context = context;
        this.navigationHost = VkTargetApplication.getNavigationHost();
    }

    @JavascriptInterface
    public void setApiKey(String apiKey){
        if(apiKey == null || apiKey.isEmpty()) {
            return;
        }
        UserData.APIKey = apiKey;
        this.navigationHost.NavigateTo(new TasksFragment(), false);
    }

    @JavascriptInterface
    public void parseAvailableTasks(String tasksHTMLPage) {
        Elements tasksElements = Jsoup.parse(tasksHTMLPage).getElementsByClass(taskItemClass);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(VkTargetApplication.getCurrentActivity());
        dialogBuilder
                .setTitle("Tasls")
                .setMessage(tasksElements.html())
                .setPositiveButton("Ну ок блін", null)
                .create().show();
    }
}
