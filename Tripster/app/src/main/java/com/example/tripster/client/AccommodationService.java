package com.example.tripster.client;

import com.example.tripster.model.Accommodation;
import com.example.tripster.model.User;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
    @GET("accommodations/{id}")
    Call<Accommodation> getAccommodation(@Path("id") long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("accommodations")
    Call<Accommodation> updateAccommodation(@Body Accommodation accommodation);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/host/{hostId}")
    Call<List<Accommodation>> getForHost(@Path("hostId") Long hostId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/admin")
    Call<List<Accommodation>> getForAdmin();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PATCH("accommodations")
    Call<String> patchStatus(@Body Accommodation accommodation);
}
