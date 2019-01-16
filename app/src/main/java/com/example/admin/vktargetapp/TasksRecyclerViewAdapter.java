package com.example.admin.vktargetapp;

import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.FinishedTask;
import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.Task;

import java.util.List;

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
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
        Task currentTask;
        if(this.showFinishedTasks) {
            currentTask = this.finishedTasks.get(i);
            String finishedAt = VkTargetApplication
                    .getCurrentActivity()
                    .getResources()
                    .getString(R.string.finished_at);
            taskViewHolder.finishDate.setText(finishedAt + " " + ((FinishedTask) currentTask).FinishedDate);
            taskViewHolder.finishDate.setVisibility(View.VISIBLE);
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

        public TaskViewHolder(View view) {
            super(view);
            siteIcon = view.findViewById(R.id.siteIcon);
            description = view.findViewById(R.id.taskDescription);
            link = view.findViewById(R.id.siteLink);
            price = view.findViewById(R.id.taskPrice);
            finishDate = view.findViewById(R.id.taskFinishDate);
        }
    }
}
