package com.example.tripster.service;

import com.example.tripster.model.view.Review;
import com.example.tripster.model.view.ReviewReport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface ReviewReportService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/reviews/reports")
    Call<List<ReviewReport>> getAccommodationReviewReports();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("users/reviews/reports")
    Call<List<ReviewReport>> getUserReviewReports();
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PATCH("accommodations/reviews/reports/{reportId}")
    Call<String> approveAccommodationReviewReports(@Path("reportId") long id);
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("accommodations/reviews/reports/{id}")
    Call<String> deleteAccommodationReviewReports(@Path("id") long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PATCH("users/reviews/reports/{reportId}")
    Call<String> approveUserReviewReports(@Path("reportId") long id);
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("users/reviews/reports/{id}")
    Call<String> deleteUserReviewReports(@Path("id") long id);
}
