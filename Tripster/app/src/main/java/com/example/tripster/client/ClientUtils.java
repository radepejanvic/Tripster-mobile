package com.example.tripster.client;

import android.util.Log;

import com.example.tripster.TripsterApp;
import com.example.tripster.BuildConfig;
import com.example.tripster.service.ReportService;
import com.example.tripster.service.ReservationService;
import com.example.tripster.service.ReviewReportService;
import com.example.tripster.service.ReviewService;
import com.example.tripster.service.UserReportService;
import com.example.tripster.service.UserService;
import com.example.tripster.util.SharedPreferencesManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {

    public static final String SERVICE_API_PATH = "http://"+ BuildConfig.IP_ADDR +":8080/api/";

    public static OkHttpClient authenticatedClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor);

        builder.addInterceptor(chain -> {
            String jwt = SharedPreferencesManager.getUserInfo(TripsterApp.getContext()).getToken();
            String header = jwt != "" ? "Bearer " + jwt : "";

            Request originalRequest = chain.request();
            Request.Builder builder1 = originalRequest.newBuilder()
                    .header("Authorization", header);
            Request newRequest = builder1.build();
            return chain.proceed(newRequest);
        });

        return builder.build();
    }

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(authenticatedClient())
            .build();

    public static AuthService authService = retrofit.create(AuthService.class);

    public static ReservationService reservationService = retrofit.create(ReservationService.class);

    public static  AccommodationService accommodationService = retrofit.create(AccommodationService.class);

    public static UserService userService = retrofit.create(UserService.class);

    public static ReviewService reviewService = retrofit.create(ReviewService.class);

    public static UserReportService userReportService = retrofit.create(UserReportService.class);

    public static ReviewReportService reviewReportService = retrofit.create(ReviewReportService.class);

    public static ReportService reportService = retrofit.create(ReportService.class);
}


