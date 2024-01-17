package com.example.tripster.service;

import com.example.tripster.model.Status;
import com.example.tripster.model.view.Notification;
import com.example.tripster.model.view.Review;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotificationService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("notifications/unread/{id}")
    Call<List<Notification>> getUnreadNotifications(@Path("id") long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("notifications/read/{id}")
    Call<List<Notification>> getReadNotifications(@Path("id") long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PATCH("notifications")
    Call<Notification> markAsRead(@Body Status status);

}
