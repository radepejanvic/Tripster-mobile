package com.example.tripster.client;

import android.view.ScaleGestureDetector;

import com.example.tripster.model.Accommodation;
import com.example.tripster.model.User;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AccommodationService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations")
    Call<Accommodation> save(@Body Accommodation accommodation);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/host/1")
    Call<List<Accommodation>> getForGuest();
}
