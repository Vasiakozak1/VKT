package com.example.admin.vktargetapp;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.Task;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TasksFragment extends Fragment {
    private Timer timer;
    private TimerTask retrieveTasksHandler;
    private View tasksView;

    private OnFragmentInteractionListener mListener;
    private List<Task> tasks;
    public TasksFragment() {
        // Required empty public constructor
        retrieveTasksHandler = new TimerTask() {
            @Override
            public void run() {
                VkTargetWebCrawler
                        .getInstance()
                        .RetrieveTasks();
            }
        };
        timer = new Timer();
    }

    public TasksFragment(List<Task> tasks) {
        this.tasks = tasks;
        retrieveTasksHandler = new TimerTask() {
            @Override
            public void run() {
                VkTargetWebCrawler
                        .getInstance()
                        .RetrieveTasks();
            }
        };
        timer = new Timer();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.tasksView = inflater.inflate(R.layout.fragment_tasks, container, false);
        VkTargetApplication.setCurrentFragment(this);
        if(tasks == null) {
            VkTargetWebCrawler
                    .getInstance()
                    .RetrieveTasks();
        }
        else {
            RecyclerView tasksList = this.tasksView.findViewById(R.id.tasksCardRecyclerView);
            tasksList.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.tasksView.getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            tasksList.setLayoutManager(layoutManager);

            TasksRecyclerViewAdapter tasksAdapter = new TasksRecyclerViewAdapter(tasks);
            tasksList.setAdapter(tasksAdapter);
            timer.schedule(this.retrieveTasksHandler, 5000);
        }

        return this.tasksView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        if(timer != null) {
            timer.cancel();
            timer.purge();
        }
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
