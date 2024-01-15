package com.example.tripster.service;

import com.example.tripster.model.view.Reservation;
import com.example.tripster.model.view.Review;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ReservationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/host/{id}")
    Call<List<Reservation>> getReservationForHost(@Path("id") long id);
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/guest/{id}")
    Call<List<Reservation>> getReservationForGuest(@Path("id") long id);
}
