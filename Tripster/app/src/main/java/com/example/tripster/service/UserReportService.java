package com.example.tripster.service;

import com.example.tripster.model.User;
import com.example.tripster.model.view.Reservation;
import com.example.tripster.model.view.Review;
import com.example.tripster.model.view.UserReport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface UserReportService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("users/reports")
    Call<List<UserReport>> getAllUserReport();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PATCH("users/reports/{reportId}")
    Call<String> approveReport(@Path("reportId") long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("users/reports/{id}")
    Call<String> deleteReport(@Path("id") long id);
}
