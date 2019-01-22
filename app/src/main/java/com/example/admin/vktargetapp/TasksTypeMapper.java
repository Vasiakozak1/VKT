package com.example.admin.vktargetapp;

import android.app.AlertDialog;

import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.FinishedTask;
import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.Task;
import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.TaskDescriptionAccordance;
import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.TaskType;
import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.TaskTypesResponseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TasksTypeMapper {
    private final String vkKeyWord = "Вконтакте";
    private final String fbKeyWord = "Facebook";
    private final String youtubeKeyWord = "youtube";
    private static TasksTypeMapper instance;
    private TaskType[] taskTypes;
    private List<TaskDescriptionAccordance> availableTasksDescriptionAccordances;
    private List<TaskDescriptionAccordance> finishedTasksDescriptionAccordances;
    private TasksTypeMapper() {
        initMapper();
    }

    public static TasksTypeMapper getInstance() {
        if(instance == null) {
            instance = new TasksTypeMapper();
        }
        return instance;
    }

    public int MapTaskType(Task task) {
        String taskSiteName = "";
        switch (task.SiteIconResourceId) {
            case R.drawable.vk_box:
                taskSiteName = vkKeyWord;
                break;
            case R.drawable.facebook_box:
                taskSiteName = fbKeyWord;
                break;
            case R.drawable.youtube:
                taskSiteName = youtubeKeyWord;
                break;
                //TODO implement task type mapping for other social networks
        }

        String currentTaskFullDescription;
        List<TaskDescriptionAccordance> accordances;
        if(task instanceof FinishedTask) {
            accordances = finishedTasksDescriptionAccordances;
            currentTaskFullDescription = task.Description;
        }
        else {
            accordances = availableTasksDescriptionAccordances;
            currentTaskFullDescription = task.Description + " " + task.LinkText;
        }


        for(TaskType taskType: taskTypes) {
            if(taskType.getTaskDescription().contains(taskSiteName)) {

                for(TaskDescriptionAccordance descriptionAccordance: accordances) {
                    if(currentTaskFullDescription.equals(descriptionAccordance.AvailableTaskDescription)
                            && taskType.getTaskDescription().contains(descriptionAccordance.TaskTypeDescription)) {
                        return taskType.getTaskType();
                    }

                }
            }
        }
        return 0;
    }

    private void initMapper() {
        retrieveTaskTypes();
        readAvailableTasksDescriptionAccordance();
        readFinishedTasksDescriptionAccordance();
    }

    private void retrieveTaskTypes() {
        String apiKey = VkTargetApplication.getApiKey();
        Retrofit vkTargetClient = NetworkClient.getVkTargetClient();
        VkTargetAPIs vkTargetAPI = vkTargetClient.create(VkTargetAPIs.class);
        Call<TaskTypesResponseObject> call = vkTargetAPI.getTaskTypes(apiKey);
        call.enqueue(new Callback<TaskTypesResponseObject>() {
            @Override
            public void onResponse(Call<TaskTypesResponseObject> call, Response<TaskTypesResponseObject> response) {
                if(response.body() != null) {
                    taskTypes = response.body()
                            .getTaskTypes();
                }
            }

            @Override
            public void onFailure(Call<TaskTypesResponseObject> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VkTargetApplication.getCurrentActivity());
                builder.setMessage(t.getMessage());
                builder.show();
            }
        });
    }

    private void readAvailableTasksDescriptionAccordance() {
        try {
            availableTasksDescriptionAccordances = new ArrayList<>();
            JSONObject rootObject = getRootOjectFromFile("AvailableTaskDescriptionAccordance.json");
            JSONArray accordancesArray = rootObject.getJSONArray("tasksDescriptionAccordances");
            for(int accordanceIndex = 0; accordanceIndex < accordancesArray.length(); accordanceIndex++) {
                JSONObject accordance = accordancesArray.getJSONObject(accordanceIndex);
                String availableTaskDescription = accordance.getString("availableTaskDescription");
                String taskTypeDescription = accordance.getString("taskTypeDescription");
                availableTasksDescriptionAccordances
                        .add(new TaskDescriptionAccordance(availableTaskDescription, taskTypeDescription));
            }
        }
        catch (JSONException exc) {
            AlertDialog.Builder builder = new AlertDialog.Builder(VkTargetApplication.getCurrentActivity());
            builder.setTitle("Помилка");
            builder.setMessage("Виникла помилка при отриманні типу завдання:\n" + exc.getMessage());
            builder.show();
        }
    }

    private void readFinishedTasksDescriptionAccordance() {
        try {
            finishedTasksDescriptionAccordances = new ArrayList<>();
            JSONObject rootObject = getRootOjectFromFile("FinishedTaskDescriptionAccordance.json");
            JSONArray accordancesArray = rootObject.getJSONArray("tasksDescriptionAccordances");
            for(int accordanceIndex = 0; accordanceIndex < accordancesArray.length(); accordanceIndex++) {
                JSONObject accordance = accordancesArray.getJSONObject(accordanceIndex);
                String availableTaskDescription = accordance.getString("finishedTaskDescription");
                String taskTypeDescription = accordance.getString("taskTypeDescription");
                finishedTasksDescriptionAccordances
                        .add(new TaskDescriptionAccordance(availableTaskDescription, taskTypeDescription));
            }
        }
        catch (JSONException exc) {
            AlertDialog.Builder builder = new AlertDialog.Builder(VkTargetApplication.getCurrentActivity());
            builder.setTitle("Помилка");
            builder.setMessage("Виникла помилка при отриманні типу завдання:\n" + exc.getMessage());
            builder.show();
        }
    }

    private JSONObject getRootOjectFromFile(String fileName) {
        try {
            InputStream stream = VkTargetApplication
                    .getAppContext()
                    .getAssets()
                    .open(fileName);
            int streamSize = stream.available();
            byte[] buffer = new byte[streamSize];
            stream.read(buffer);
            stream.close();
            String json = new String(buffer, "UTF-8");
            return new JSONObject(json);
        }
        catch (IOException exc) {

        }
        catch (JSONException exc){

        }
        return null;
    }
}
