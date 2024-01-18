package com.example.tripster.service;

import com.example.tripster.model.view.Interval;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AvailabilityService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations/price/{id}")
    Call<Integer> addCalendar(@Path("id") long id, @Body List<Interval> intervals);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("accommodations/price/{id}")
    Call<Integer> updateCalendar(@Path("id") long id, @Body List<Interval> intervals);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations/calendar/{id}")
    Call<Integer> disableDays(@Path("id") long id, @Body Interval interval);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/pricelists/{id}")
    Call<List<Interval>> getCalendar(@Path("id") long id);

}
