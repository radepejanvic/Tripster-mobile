package com.example.tripster.fragment.reviews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripster.adapters.ReservationListAdapter;
import com.example.tripster.adapters.ReviewReportListAdapter;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentReservationListBinding;
import com.example.tripster.model.view.Reservation;
import com.example.tripster.model.view.ReviewReport;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReviewReportlListFragment extends ListFragment {

    private static final String ARG_PARAM = "param";

    private Long mode;
    private FragmentReservationListBinding binding;

    private ReviewReportListAdapter adapter;


    public static ReviewReportlListFragment newInstance(ArrayList<Reservation> Reservations){
        ReviewReportlListFragment fragment = new ReviewReportlListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, Reservations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getLong("mode");
            adapter = new ReviewReportListAdapter(getActivity(), new ArrayList<>(),mode);
            setListAdapter(adapter);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("Tripster", "onCreateView: ReviewReportlListFragment, bundle: " + getArguments());

        binding = FragmentReservationListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (mode == 1){
            getAccommodationReviewReport();
        }else {
            getUserReviewReport();
        }


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getAccommodationReviewReport() {
        Call<List<ReviewReport>> call = ClientUtils.reviewReportService.getAccommodationReviewReports();

        call.enqueue(new Callback<List<ReviewReport>>() {
            @Override
            public void onResponse(Call<List<ReviewReport>> call, Response<List<ReviewReport>> response) {

                if(response.code() == 200) {
                    setAdapter(response.body());
                } else {
                    Log.e("GET Request", "Error fetching user " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ReviewReport>> call, Throwable t) {

            }
        });
    }

    private void getUserReviewReport() {
        Call<List<ReviewReport>> call = ClientUtils.reviewReportService.getUserReviewReports();

        call.enqueue(new Callback<List<ReviewReport>>() {
            @Override
            public void onResponse(Call<List<ReviewReport>> call, Response<List<ReviewReport>> response) {

                if(response.code() == 200) {
                    setAdapter(response.body());
                } else {
                    Log.e("GET Request", "Error fetching user " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ReviewReport>> call, Throwable t) {

            }
        });
    }
    private void setAdapter(List<ReviewReport> reviewReports) {
        this.adapter.addAll(reviewReports);
    }


}