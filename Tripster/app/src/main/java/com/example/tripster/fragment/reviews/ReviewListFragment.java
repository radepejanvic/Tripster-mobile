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

import com.example.tripster.adapters.ReviewListAdapter;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentReviewListBinding;
import com.example.tripster.databinding.FragmentReviewsBinding;
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

    private ArrayList<Review> reviews;

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
            reviews = new ArrayList<>();
            adapter = new ReviewListAdapter(getActivity(), reviews);
            setListAdapter(adapter);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("Tripster", "onCreateView: ReviewsFragment, bundle: " + getArguments());

        binding = FragmentReviewListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        reviews.clear();

        getAccommodationReviews(1l);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getAccommodationReviews(Long accommodationId) {
        Log.d("GET ACCOMMODATION", "ID: " + accommodationId);
        Call<List<Review>> call = ClientUtils.reviewService.getAccommodationReviews(accommodationId);

        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if(response.code() == 200) {

                    reviews.addAll(response.body());
                    setAdapter(reviews);

                    Log.d("GET Request", "Reviews: " + reviews);
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

    }

    private void setAdapter(List<Review> reviews) {
        this.adapter.addAll(reviews);
    }


}