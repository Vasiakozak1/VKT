package com.example.admin.vktargetapp;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.Task;
import com.example.admin.vktargetapp.task_executors.ITaskExecutor;
import com.example.admin.vktargetapp.task_executors.YoutubeTaskExecutor;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TasksFragment extends Fragment implements IWebViewCreator {
    private static View SavedView;
    private Timer timer;
    private TimerTask retrieveTasksHandler;
    private View tasksView;
    private TasksTypeMapper tasksTypeMapper;
    private LinearLayout tasksLayout;
    private Button buttonOfTaskToCheck;


    private OnFragmentInteractionListener mListener;
    private List<Task> tasks;
    public TasksFragment() {
        // Required empty public constructor
        retrieveTasksHandler = new TimerTask() {
            @Override
            public void run() {
                VkTargetApplication.setLoading();
                VkTargetWebCrawler
                        .getInstance()
                        .RetrieveTasks();
            }
        };
        timer = new Timer();
        VkTargetApplication.setCurrentFragment(this);
    }

    public TasksFragment(List<Task> tasks) {
        this.tasks = tasks;
        VkTargetApplication.setCurrentFragment(this);
        retrieveTasksHandler = new TimerTask() {
            @Override
            public void run() {
                VkTargetApplication.setLoading();
                VkTargetWebCrawler
                        .getInstance()
                        .RetrieveTasks();
            }
        };
        timer = new Timer();
    }

    public void UpdateTasks(List<Task> newTasks){
        RecyclerView tasksList = this.tasksView.findViewById(R.id.tasksCardRecyclerView);
        //tasksList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.tasksView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tasksList.setLayoutManager(layoutManager);

        TasksRecyclerViewAdapter tasksAdapter = new TasksRecyclerViewAdapter(newTasks);
        tasksList.setAdapter(tasksAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.tasksView.getContext());
        builder.setMessage("adapter set");
        builder.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(SavedView == null) {
            this.tasksView = inflater.inflate(R.layout.fragment_tasks, container, false);
            SavedView = this.tasksView;
        }
        else {
            this.tasksView = SavedView;
        }

        tasksTypeMapper = TasksTypeMapper.getInstance();
        TextView noTsksTextView = this.tasksView.findViewById(R.id.noTasksTextView);

        noTsksTextView.setVisibility(View.VISIBLE);
        if(tasks == null) {
            VkTargetApplication.setLoading();
            VkTargetWebCrawler
                    .getInstance()
                          .RetrieveTasks();
        }
        else {
            checkMenuItem();
            if(tasks.size() > 0) {
                noTsksTextView.setVisibility(View.INVISIBLE);
                RecyclerView tasksList = this.tasksView.findViewById(R.id.tasksCardRecyclerView);
                //tasksList.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this.tasksView.getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                tasksList.setLayoutManager(layoutManager);

                TasksRecyclerViewAdapter tasksAdapter = new TasksRecyclerViewAdapter(tasks);
                tasksList.setAdapter(tasksAdapter);
            }
            else {
                noTsksTextView.setVisibility(View.VISIBLE);
            }


            VkTargetApplication.setLoaded();
        }
        timer.schedule(this.retrieveTasksHandler, 10000);
        return this.tasksView;
    }

    @Override
    public WebView CreateWebView() {
        tasksLayout = this.tasksView.findViewById(R.id.tasksLayout);
        WebView webView = new WebView(this.tasksView.getContext());

        Random random = new Random();
        int randomId = random.nextInt();
        webView.setId(randomId);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1000,1000);
        webView.setLayoutParams(layoutParams);
        this.tasksLayout.addView(webView,1);
        return webView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setButtonForTaskChecking(Button button) {
        this.buttonOfTaskToCheck = button;
    }

    public void setTaskIsComleted() {
        this.buttonOfTaskToCheck.setBackgroundColor(getResources().getColor(R.color.finishedTaskButtonColor));
        this.buttonOfTaskToCheck.setText("Task is completed");
    }
    public void setTaskIsNotCompleted() {
        this.buttonOfTaskToCheck.setText("Task is not completed");
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

    private void checkMenuItem() {
        NavigationView navigationView = VkTargetApplication.getCurrentActivity()
                .findViewById(R.id.nav_view);
        navigationView.getMenu()
                .findItem(R.id.tasksBtn)
                .setChecked(true);
    }
}
