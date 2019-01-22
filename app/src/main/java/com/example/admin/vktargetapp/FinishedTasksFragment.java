package com.example.admin.vktargetapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.FinishedTask;

import org.w3c.dom.Text;

import java.util.List;

public class FinishedTasksFragment extends Fragment {
    private List<FinishedTask> finishedTasks;

    public FinishedTasksFragment(){
    }

    public FinishedTasksFragment(List<FinishedTask> finishedTasks) {
        this.finishedTasks = finishedTasks;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_finished_tasks, container, false);
        if(finishedTasks == null) {
            VkTargetApplication.setLoading();
            VkTargetWebCrawler
                    .getInstance()
                    .RetrieveFinishedTasks();
        }
        else {
            checkMenuItem();
            TextView noTasksTextView = view.findViewById(R.id.noFinishedTasksTextView);
            if(finishedTasks.size() > 0) {
                noTasksTextView.setVisibility(View.INVISIBLE);
                RecyclerView tasksList = view.findViewById(R.id.finishedtasksCardRecyclerView);
                tasksList.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                tasksList.setLayoutManager(layoutManager);

                TasksRecyclerViewAdapter tasksAdapter = new TasksRecyclerViewAdapter(finishedTasks, true);
                tasksList.setAdapter(tasksAdapter);
            }
            else
            {
                noTasksTextView.setVisibility(View.VISIBLE);
            }
            VkTargetApplication.setLoaded();
        }
        return view;
    }

    private void checkMenuItem() {
        NavigationView navigationView = VkTargetApplication.getCurrentActivity()
                .findViewById(R.id.nav_view);
        navigationView.getMenu()
                .findItem(R.id.doneTasks)
                .setChecked(true);
    }
}
