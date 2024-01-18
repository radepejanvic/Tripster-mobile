package com.example.tripster.service;

import com.example.tripster.model.Settings;
import com.example.tripster.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SettingsService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("users/settings/{userId}")
    Call<Settings> getSettings(@Path("userId") long userId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("users/settings")
    Call<Settings> changeSettings(@Body Settings settings);

}
