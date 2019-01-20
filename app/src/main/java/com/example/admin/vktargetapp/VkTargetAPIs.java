package com.example.admin.vktargetapp;

import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.TaskType;
import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.TaskTypesResponseObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VkTargetAPIs {

    @GET("method/getTypes")
    Call<TaskTypesResponseObject> getTaskTypes(@Query("api_key") String apiKey);
}
