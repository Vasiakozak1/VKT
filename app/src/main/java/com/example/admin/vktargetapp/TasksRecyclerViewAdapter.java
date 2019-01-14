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

import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.Task;

import java.util.List;

public class TasksRecyclerViewAdapter  extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TaskViewHolder>{

    private List<Task> tasks;

    public TasksRecyclerViewAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getItemCount() {
        return this.tasks.size();
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
        Task currentTask = this.tasks.get(i);
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

        public TaskViewHolder(View view) {
            super(view);
            siteIcon = view.findViewById(R.id.siteIcon);
            description = view.findViewById(R.id.taskDescription);
            link = view.findViewById(R.id.siteLink);
            price = view.findViewById(R.id.taskPrice);
        }
    }
}
