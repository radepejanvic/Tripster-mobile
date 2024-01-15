package com.example.tripster;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentAccountBinding;
import com.example.tripster.databinding.FragmentSingleAccommodationBinding;
import com.example.tripster.model.Accommodation;
import com.example.tripster.util.SharedPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleAccommodationFragment extends Fragment {

    private FragmentSingleAccommodationBinding binding;

    private Accommodation accommodation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSingleAccommodationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // TODO: Change hardcoded id to dynamically chosen one
        getAccommodation(1);

        binding.reviews.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong("accommodationId", accommodation.getId());
            bundle.putLong("hostId", accommodation.getOwnerUserId());
            findNavController(v).navigate(R.id.action_singleAccommodationFragment_to_navigation_reviews, bundle);
        });


        return root;
    }

    private void getAccommodation(long id){
        Log.d("RADE RADE RADE RADE RADE", "USAO U GET ACCOMMODATION");
        Call<Accommodation> call = ClientUtils.accommodationService.getAccommodation(id);

        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.code() == 200){
                    accommodation = response.body();
                    Log.d("GET Request", "Accommodation " + response.body());
                } else {
                    Log.d("GET Request", "Error fetching accommodation " + response.body());
                }
            }

            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {

            }
        });

    }
}