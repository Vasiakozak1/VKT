package com.example.admin.vktargetapp;


import android.app.AlertDialog;

import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.FinishedTask;
import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.Task;
import org.jsoup.nodes.Element;


public class TaskBuilder {
    private final String youtubeIconClass = "social__img youtube";
    private final String twitterIconClass = "social__img twitter";
    private final String vkIconClass = "social__img vk";
    private final String googleplusIconClass = "fa fa-google-plus";
    private final String odnoklasnikiIconClass = "social__img odnoklassniki";

    private final String taskItemClass = "vkt-content__list-item";
    private final String taskItemDataClass = "vkt-content__list-data";
    private final String taskPriceClass = "vkt-popup__task-details-arrow";
    private final String tipElementId = "tip";

    private Element taskElement;
    private int taskResourceId;
    private String taskDescription;
    private double taskPrice;
    private String taskLinkUrl;
    private String taskLinkText;
    private String finishingDate;
    public TaskBuilder(Element taskElement) {
        this.taskElement = taskElement;
    }

    public TaskBuilder addTaskIconResuourceId() {
        String iconClass;
        if(hasAdditionalWrapper()) {
            iconClass = taskElement
                    .child(0)
                    .child(0)
                    .child(0)
                    .attr("class");
        }
        else {
            iconClass = taskElement
                    .child(0)
                    .child(0)
                    .attr("class");
        }

        switch (iconClass) {
            case youtubeIconClass:
                taskResourceId = R.drawable.youtube;
                break;
            case twitterIconClass:
                taskResourceId = R.drawable.twitter_box;
                break;
            case vkIconClass:
                taskResourceId = R.drawable.vk_box;
                break;
            case googleplusIconClass:
                taskResourceId = R.drawable.google_plus;
                break;
            case odnoklasnikiIconClass:
                taskResourceId = R.drawable.odnoklassniki;
                break;
        }
        return this;
    }

    public TaskBuilder addDescription(){
        String taskPriceText = taskElement.child(1)
                .getElementsByTag("span")
                .first()
                .text();
        this.taskPrice = Double.parseDouble(taskPriceText);
        Element wrapElementForDescription = taskElement.child(2)
                .child(0);
        taskDescription = wrapElementForDescription.getElementsByTag("span")
                .first()
                .text();
        taskLinkUrl = wrapElementForDescription.getElementsByTag("a")
                .first()
                .attr("href");
        taskLinkText = wrapElementForDescription.getElementsByTag("a")
                .first()
                .text();


        return this;
    }

    public TaskBuilder addFInishDate() {
        Element finishedDateElement = taskElement.selectFirst("span[data-bind='date']");
        this.finishingDate = finishedDateElement.text();
        return this;
    }

    public Task BuildTask() {
        return new Task(this.taskDescription, this.taskResourceId, this.taskPrice, this.taskLinkUrl, this.taskLinkText);
    }
    public FinishedTask BuildFinishedTask() {
        return new FinishedTask(this.taskDescription, this.taskResourceId, this.taskPrice, this.taskLinkUrl, this.taskLinkText, this.finishingDate);
    }

    private boolean hasAdditionalWrapper() {
        String wrapperClass = taskElement
                .child(0)
                .child(0)
                .attr("class");
        return wrapperClass.equals("inside");
    }
}
