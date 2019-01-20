package com.example.admin.vktargetapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    public static final String VkTargetBaseUrl = "https://vktarget.ru/api/";

    private static Retrofit retrofitClient;
    public static Retrofit getVkTargetClient() {
        if(retrofitClient == null) {
            retrofitClient = new Retrofit.Builder()
                    .baseUrl(VkTargetBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitClient;
    }
}
