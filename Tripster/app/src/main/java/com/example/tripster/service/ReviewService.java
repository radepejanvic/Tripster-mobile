package com.example.tripster.service;

import com.example.tripster.model.Accommodation;
import com.example.tripster.model.User;
import com.example.tripster.model.view.Review;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReviewService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/reviews/{id}")
    Call<List<Review>> getAccommodationReviews(@Path("id") long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("users/reviews/{id}")
    Call<List<Review>> getHostReviews(@Path("id") long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations/reviews")
    Call<Review> addAccommodationReview(@Body Review review);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("users/reviews")
    Call<Review> addHostReview(@Body Review review);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("accommodations/reviews/{id}")
    Call<Boolean> deleteAccommodationReview(@Path("id") long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("users/reviews/{id}")
    Call<Boolean> deleteHostReview(@Path("id") long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/reviews/{accommodationId}/{guestId}")
    Call<Boolean> canReviewAccommodation(@Path("accommodationId") long accommodationId, @Path("guestId") long guestId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("users/reviews/{hostId}/{guestId}")
    Call<Boolean> canReviewHost(@Path("hostId") long accommodationId, @Path("guestId") long guestId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/reviews/new")
    Call<List<Review>> getAllReviews();
}
