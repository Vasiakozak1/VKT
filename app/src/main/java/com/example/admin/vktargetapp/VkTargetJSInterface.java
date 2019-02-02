package com.example.admin.vktargetapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
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
    private final String taskItemClass = "row tb__row ";
    private final String lastTaskItemClass = "row tb__row last";
    private final String noApiKeyLabel = "У вас отсутствует API Key";

    private final int COUNT_OF_NOT_LOGGED_IN_USER_ELEMENTS = 0;
    private final int COUNT_OF_LOGGED_IN_USER_ELEMENTS = 1;

    private Context context;
    private NavigationHost navigationHost;
    private int countOfTryingToCheckTask = 0;

    public  VkTargetJSInterface(Context context){
        this.context = context;
        this.navigationHost = VkTargetApplication.getNavigationHost();
    }

    @JavascriptInterface
    public void setApiKey(final int countOfLoggedInElements,final String apikeyHtmlSection, final String apiKey){
        // This method runs two times instead of one
        VkTargetApplication.getCurrentActivity()
                .runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(countOfLoggedInElements == COUNT_OF_NOT_LOGGED_IN_USER_ELEMENTS) {
                            LoginFragment loginFragment =(LoginFragment) VkTargetApplication.getCurrentFragment();
                            loginFragment.ShowWrongCredentialsMessage();
                        }
                        Element apiKeyDocumentSection = Jsoup.parse(apikeyHtmlSection);
                        if(apiKeyDocumentSection.text().contains(noApiKeyLabel)) {
                            VkTargetWebCrawler
                                    .getInstance()
                                    .GenerateApiKey();
                            return;
                        }
                        //String apiKey = apiKeyElement.attr("value");
                        if(apiKey == null || apiKey.isEmpty()) {
                            VkTargetApplication.setLoaded();
                            return;
                        }
                        VkTargetApplication.enableNavigationMenu();
                        VkTargetApplication.setApiKey(apiKey);
                        navigationHost.NavigateTo(new TasksFragment(), false);

                    }
                });

    }

    @JavascriptInterface
    public void checkIsCredentialsRight(int countOfLoggedInElements) {
        if(countOfLoggedInElements == COUNT_OF_NOT_LOGGED_IN_USER_ELEMENTS) {
            LoginFragment loginFragment =(LoginFragment) VkTargetApplication.getCurrentFragment();
            loginFragment.ShowWrongCredentialsMessage();
        }
        else {
            LoginFragment loginFragment =(LoginFragment) VkTargetApplication.getCurrentFragment();
            loginFragment.ShowWrongCredentialsMessage();
        }

    }

    @JavascriptInterface
    public void setLoginNeeded(int countOfLoggedOnUserElements) {
        if(countOfLoggedOnUserElements == COUNT_OF_LOGGED_IN_USER_ELEMENTS) {
            Session.NeedsLogin = NeedsLogin.No;
        }
        else if(countOfLoggedOnUserElements == COUNT_OF_NOT_LOGGED_IN_USER_ELEMENTS) {
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
    public void parseAvailableTasks(final String tasksHTMLPage) {

        Elements tasksElements = getTasksElements(tasksHTMLPage, false);
        final List<Task> availableTasks = retrieveTasksFromElements(tasksElements);

        TasksTypeMapper mapper = TasksTypeMapper
                .getInstance();
        for(Task taskToGetType: availableTasks) {
            taskToGetType.Type = mapper.MapTaskType(taskToGetType);
        }
        navigationHost.NavigateTo(new TasksFragment(availableTasks), false);
    }

    @JavascriptInterface
    public void parseDoneTasks(String tasksHTMLPage) {
        Elements tasksElements = getTasksElements(tasksHTMLPage, true);

        List<FinishedTask> finishedTasks = retrieveFinishedTasksFromElements(tasksElements);
        TasksTypeMapper mapper = TasksTypeMapper
                .getInstance();
        for(FinishedTask taskToGetType: finishedTasks) {
            taskToGetType.Type = mapper.MapTaskType(taskToGetType);
        }
        this.navigationHost.NavigateTo(new FinishedTasksFragment(finishedTasks), false);
    }

    public void checkIsTaskDone(int countOfSuccessElements) {
        countOfTryingToCheckTask++;
        TasksFragment tasksFragment = (TasksFragment) VkTargetApplication.getCurrentFragment();
        if(countOfSuccessElements >= 1) {
            tasksFragment.setTaskIsComleted();
            return;
        }
        if(countOfTryingToCheckTask == 2 && countOfSuccessElements == 0) {
            tasksFragment.setTaskIsNotCompleted();
        }
    }

    private Elements getTasksElements(String tasksHtmlPage, boolean finishedTasks) {
        Document tasksDocument = Jsoup.parse(tasksHtmlPage);
        Element container;
        Elements taskElements;
        if(finishedTasks) {
            container = tasksDocument.getElementsByClass("container-fluid available__table")
                    .last();
            taskElements = container.children();
        }
        else {
            container = tasksDocument.getElementsByClass("container-fluid available__table")
                    .first();
            taskElements = container.getElementsByClass(taskItemClass);
            Element lastTask = container.getElementsByClass(lastTaskItemClass).first();
            taskElements.add(lastTask);
        }

        return taskElements;
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
