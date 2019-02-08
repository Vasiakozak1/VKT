package com.example.admin.vktargetapp;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.FinishedTask;
import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.TaskData;
import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.Task;
import com.example.admin.vktargetapp.task_executors.ITaskExecutor;
import com.example.admin.vktargetapp.task_executors.YoutubeTaskExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksRecyclerViewAdapter  extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TaskViewHolder>{

    private List<Task> tasks;
    private List<FinishedTask> finishedTasks;
    private boolean showFinishedTasks;

    public TasksRecyclerViewAdapter(List<Task> tasks) {
        this.tasks = tasks;
        this.showFinishedTasks = false;
    }
    public TasksRecyclerViewAdapter(List<FinishedTask> finishedTasks, boolean showFinishDate) {
        this.showFinishedTasks = showFinishDate;
        this.finishedTasks = finishedTasks;
    }

    @Override
    public int getItemCount() {
        return showFinishedTasks ? finishedTasks.size() : tasks.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder taskViewHolder, final int i) {
        final Task currentTask;
        if(this.showFinishedTasks) {
            currentTask = this.finishedTasks.get(i);
            String finishedAt = VkTargetApplication
                    .getCurrentActivity()
                    .getResources()
                    .getString(R.string.finished_at);
            taskViewHolder.finishDate.setText(finishedAt + " " + ((FinishedTask) currentTask).FinishedDate);
            taskViewHolder.finishDate.setVisibility(View.VISIBLE);
            taskViewHolder.checkTaskButton.setVisibility(View.INVISIBLE);
            taskViewHolder.completeTaskButton.setVisibility(View.INVISIBLE);
        }
        else {
            currentTask = this.tasks.get(i);
            taskViewHolder.finishDate.setVisibility(View.INVISIBLE);
        }
        taskViewHolder.siteIcon.setImageResource(currentTask.SiteIconResourceId);
        taskViewHolder.description.setText(currentTask.Description);
        taskViewHolder.link.setText(Html
                .fromHtml(String.format("<a href='%s'>%s</a>", currentTask.LinkUrl, currentTask.LinkText)));
        taskViewHolder.link.setMovementMethod(LinkMovementMethod.getInstance());
        taskViewHolder.price.setText(String.valueOf(currentTask.Price) + " за виконання");
        taskViewHolder.price.setChipIconResource(R.drawable.currency_rub);
        taskViewHolder.type.setText("Тип: " + currentTask.Type);

        TaskDataStorage
                .getInstance()
                .addCheckTaskButton(currentTask.LinkUrl, currentTask.SiteIconResourceId, taskViewHolder.checkTaskButton);
        taskViewHolder.checkTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TaskData webViewForCheckingTask = TaskDataStorage
                        .getInstance()
                        .getWebView(currentTask.LinkUrl, currentTask.SiteIconResourceId);
                if(webViewForCheckingTask != null) {
                    webViewForCheckingTask.webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
                    VkTargetWebCrawler.getInstance()
                            .CheckTask(currentTask.LinkUrl, i, webViewForCheckingTask);
                }
            }
        });
        taskViewHolder.completeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView webView =((IWebViewCreator) VkTargetApplication.getCurrentFragment()).CreateWebView();
                ITaskExecutor taskExecutor = new YoutubeTaskExecutor(webView);
                taskExecutor.ExecuteTask(currentTask.Type, currentTask.LinkUrl);
            }
        });
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemvView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.task_item, viewGroup, false);

        return new TaskViewHolder(itemvView);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        protected ImageView siteIcon;
        protected TextView description;
        protected TextView link;
        protected Chip price;
        protected TextView finishDate;
        protected TextView type;
        protected Button checkTaskButton;
        protected Button completeTaskButton;

        public TaskViewHolder(View view) {
            super(view);
            siteIcon = view.findViewById(R.id.siteIcon);
            description = view.findViewById(R.id.taskDescription);
            link = view.findViewById(R.id.siteLink);
            price = view.findViewById(R.id.taskPrice);
            finishDate = view.findViewById(R.id.taskFinishDate);
            type = view.findViewById(R.id.taskType);
            checkTaskButton = view.findViewById(R.id.checkTaskBtn);
            checkTaskButton.setEnabled(false);
            completeTaskButton = view.findViewById(R.id.completeTaskBtn);
        }
    }
}
