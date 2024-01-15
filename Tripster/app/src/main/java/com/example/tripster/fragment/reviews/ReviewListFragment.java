package com.example.tripster.fragment.reviews;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tripster.R;
import com.example.tripster.adapters.ReviewListAdapter;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentReviewListBinding;
import com.example.tripster.databinding.FragmentReviewsBinding;
import com.example.tripster.model.enums.Mode;
import com.example.tripster.model.view.Product;
import com.example.tripster.model.view.Review;
import com.example.tripster.products.AccommtionListFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewListFragment extends ListFragment {

    private static final String ARG_PARAM = "param";

    private FragmentReviewListBinding binding;

    private ReviewListAdapter adapter;

    private Long accommodationId;

    private Long hostId;


    public static ReviewListFragment newInstance(ArrayList<Review> reviews){
        ReviewListFragment fragment = new ReviewListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, reviews);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adapter = new ReviewListAdapter(getActivity(), new ArrayList<>());
            setListAdapter(adapter);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (getArguments() != null) {
            accommodationId = getArguments().getLong("accommodationId");
            hostId = getArguments().getLong("hostId");
            Toast.makeText(getContext(), " Accommodation: " + accommodationId + " -- " + " Host: " + hostId, Toast.LENGTH_SHORT).show();
        }

        adapter.clear();

        spinnerSetup(binding.type, R.array.review_types);

        // TODO: Get accommodation and host id from bundle
//        getAccommodationReviews(1l);
//        getAccommodationReviews(6l);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getAccommodationReviews() {
        Call<List<Review>> call = ClientUtils.reviewService.getAccommodationReviews(accommodationId);

        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if(response.code() == 200) {

                    setAdapter(response.body());

                    Log.d("GET Request", "Accommodation reviews count: " + response.body().size());
                } else {
                    Log.e("GET Request", "Error fetching user " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {

            }
        });
    }

    private void getHostReviews() {
        Call<List<Review>> call = ClientUtils.reviewService.getHostReviews(hostId);

        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if(response.code() == 200) {

                    setAdapter(response.body());

                    Log.d("GET Request", "Host reviews count: " + response.body().size());
                } else {
                    Log.e("GET Request", "Error fetching host reviews " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {

            }
        });
    }


    private void spinnerSetup(Spinner spinner, int optionsResId) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch(position) {
                    case 0:
                       getAccommodationReviews();
                       break;
                    case 1:
                        getHostReviews();
                        break;
               }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
            }
        });

        String[] options = getResources().getStringArray(optionsResId);

        // Create a custom adapter to disable the first item
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(
                spinner.getContext(),
                android.R.layout.simple_spinner_item,
                options
        );

        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(stringArrayAdapter);
    }

    private void setAdapter(List<Review> reviews) {
        adapter.clear();
        this.adapter.addAll(reviews);
        adapter.notifyDataSetChanged();
    }


}