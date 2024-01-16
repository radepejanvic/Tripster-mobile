package com.example.tripster.service;


import com.example.tripster.model.Report;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ReportService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("users/reports")
    Call<Report> reportUser(@Body Report review);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("users/reviews/reports")
    Call<Report> reportHostReview(@Body Report review);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations/reviews/reports")
    Call<Report> reportAccommodationReview(@Body Report review);

}
