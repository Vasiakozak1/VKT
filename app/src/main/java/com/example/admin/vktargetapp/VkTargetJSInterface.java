package com.example.admin.vktargetapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.FinishedTask;
import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.Task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VkTargetJSInterface {
    private final String taskItemClass = "vkt-content__list-item";
    private final String tipElementId = "tip";

    private final int COUNT_OF_NOT_LOGGED_IN_USER_ELEMENTS = 1;
    private final int COUNT_OF_LOGGED_IN_USER_ELEMENTS = 2;

    private Context context;
    private NavigationHost navigationHost;

    public  VkTargetJSInterface(Context context){
        this.context = context;
        this.navigationHost = VkTargetApplication.getNavigationHost();
    }

    @JavascriptInterface
    public void setApiKey(int countOfLoggedInElements, String apiKey){
      //  if(countOfLoggedInElements == COUNT_OF_NOT_LOGGED_IN_USER_ELEMENTS) {
      //      LoginFragment loginFragment =(LoginFragment) VkTargetApplication.getCurrentFragment();
      //      loginFragment.ShowWrongCredentialsMessage(1);
     //   }
    //    else {
     //       LoginFragment loginFragment =(LoginFragment) VkTargetApplication.getCurrentFragment();
   //         loginFragment.ShowWrongCredentialsMessage(2);
   //     }
        if(apiKey == null || apiKey.isEmpty()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(VkTargetApplication.getCurrentActivity());
        builder.setMessage("key:" + apiKey);
        builder.show();
        VkTargetApplication.setApiKey(apiKey);
        this.navigationHost.NavigateTo(new TasksFragment(), false);
    }

    @JavascriptInterface
    public void checkIsCredentialsRight(int countOfLoggedInElements) {
        if(countOfLoggedInElements == COUNT_OF_NOT_LOGGED_IN_USER_ELEMENTS) {
            LoginFragment loginFragment =(LoginFragment) VkTargetApplication.getCurrentFragment();
            loginFragment.ShowWrongCredentialsMessage(1);
        }
        else {
            LoginFragment loginFragment =(LoginFragment) VkTargetApplication.getCurrentFragment();
            loginFragment.ShowWrongCredentialsMessage(2);
        }

    }

    @JavascriptInterface
    public void setLoginNeeded(int countOfLoggedOnUserElements) {
        if(countOfLoggedOnUserElements >= COUNT_OF_LOGGED_IN_USER_ELEMENTS) {
            Session.NeedsLogin = NeedsLogin.No;
        }
        else if(countOfLoggedOnUserElements <= COUNT_OF_NOT_LOGGED_IN_USER_ELEMENTS) {
            Session.NeedsLogin = NeedsLogin.Yes;
        }
        final Activity currentActivity = VkTargetApplication.getCurrentActivity();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try{
                    MainActivity mainActivity = (MainActivity) currentActivity;
                    if(mainActivity != null) {
                        mainActivity.ManageLogin();
                    }
                }catch (IOException e) {
                }
            }
        });

    }

    @JavascriptInterface
    public void parseAvailableTasks(String tasksHTMLPage) {
        Elements tasksElements = getTasksElements(tasksHTMLPage);
        List<Task> availableTasks = retrieveTasksFromElements(tasksElements);
        TasksTypeMapper mapper = TasksTypeMapper
                .getInstance();
        for(Task taskToGetType: availableTasks) {
            taskToGetType.Type = mapper.MapTaskType(taskToGetType);
        }
        this.navigationHost.NavigateTo(new TasksFragment(availableTasks), false);
    }

    @JavascriptInterface
    public void parseDoneTasks(String tasksHTMLPage) {
        Elements tasksElements = getTasksElements(tasksHTMLPage);
        List<FinishedTask> finishedTasks = retrieveFinishedTasksFromElements(tasksElements);
        this.navigationHost.NavigateTo(new FinishedTasksFragment(finishedTasks), false);
    }

    private Elements getTasksElements(String tasksHtmlPage) {
        Document tasksDocument = Jsoup.parse(tasksHtmlPage);
        Element superfluousTip = tasksDocument.getElementById(tipElementId);
        Elements tasksElements = tasksDocument.getElementsByClass(taskItemClass);
        tasksElements.remove(superfluousTip);
        return tasksElements;
    }

    private List<Task> retrieveTasksFromElements(Elements tasks) {
        List<Task> tasksList = new ArrayList<Task>();
        TaskBuilder taskBuilder;
        for(Element task : tasks) {
            taskBuilder = new TaskBuilder(task);
            Task buildedTask = taskBuilder.addTaskIconResuourceId()
                    .addDescription()
                    .BuildTask();
            tasksList.add(buildedTask);
        }
        return tasksList;
    }
    private List<FinishedTask> retrieveFinishedTasksFromElements(Elements tasks) {
        List<FinishedTask> tasksList = new ArrayList<FinishedTask>();
        TaskBuilder taskBuilder;
        for(Element task : tasks) {
            taskBuilder = new TaskBuilder(task);
            FinishedTask buildedTask = taskBuilder.addTaskIconResuourceId()
                    .addDescription()
                    .addFInishDate()
                    .BuildFinishedTask();
            tasksList.add(buildedTask);
        }
        return tasksList;
    }
}
