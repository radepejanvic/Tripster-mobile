package com.example.tripster.fragment.reviews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripster.R;
import com.example.tripster.adapters.ReviewListAdapter;
import com.example.tripster.adapters.ReviewReportListAdapter;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentReviewApprovalListBinding;
import com.example.tripster.databinding.FragmentReviewListBinding;
import com.example.tripster.databinding.FragmentReviewsApprovalBinding;
import com.example.tripster.model.view.Review;
import com.example.tripster.model.view.Review;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReviewApprovalListFragment extends ListFragment {

    private static final String ARG_PARAM = "param";

    private FragmentReviewApprovalListBinding binding;

    private ReviewListAdapter adapter;


    public static ReviewApprovalListFragment newInstance(ArrayList<Review> Reviews){
        ReviewApprovalListFragment fragment = new ReviewApprovalListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, Reviews);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ReviewListAdapter(getActivity(), new ArrayList<>());
        setListAdapter(adapter);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("Tripster", "onCreateView: ReviewApprovalListFragment, bundle: " + getArguments());

        binding = FragmentReviewApprovalListBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        getAccommodationReviews();
        return root;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void getAccommodationReviews() {
        Call<List<Review>> call = ClientUtils.reviewService.getAllReviews();

        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {

                if(response.code() == 200) {
                    setAdapter(response.body());
                } else {
                    Log.e("GET Request", "Error fetching user " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {

            }
        });
    }
    private void setAdapter(List<Review> reviews) {
        this.adapter.addAll(reviews);
    }
}