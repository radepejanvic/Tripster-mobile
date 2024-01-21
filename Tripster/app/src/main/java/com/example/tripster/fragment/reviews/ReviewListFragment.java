package com.example.tripster.fragment.reviews;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tripster.R;
import com.example.tripster.adapters.ReviewListAdapter;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentReviewListBinding;
import com.example.tripster.fragment.reports.ReportDialog;
import com.example.tripster.model.enums.ReportType;
import com.example.tripster.model.enums.ReviewType;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.model.view.Review;
import com.example.tripster.util.SharedPreferencesManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    private Long hostIdForCheck;

    private String mode;

    private FloatingActionButton fab;

    private boolean canReviewAccommodation;

    private boolean canReviewHost;

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
            hostIdForCheck = getArguments().getLong("hostIdForCheck");
        }

        adapter.clear();

        spinnerSetup(binding.type, R.array.review_types);

        fab = binding.fab;

        canReview(true);
        canReview(false);
        customizeFabVisibility(canReviewAccommodation);


        fab.setOnClickListener(v -> {
            ReviewType type = mode == "accommodation" ? ReviewType.ACCOMMODATION : ReviewType.HOST;
            Long id = mode == "accommodation" ? accommodationId : hostId;
            Dialog reviewDialog =new ReviewDialog(getContext(), SharedPreferencesManager.getUserInfo(getContext()).getId(), id, type);
            reviewDialog.setCancelable(true);
            reviewDialog.setCanceledOnTouchOutside(true);
            reviewDialog.show();

        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void customizeFabVisibility(boolean canReview) {
        UserType userType = SharedPreferencesManager.getUserInfo(getContext()).getUserType();

        if (userType != UserType.GUEST || !canReview) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }

//        if (userType != UserType.GUEST || !canReview) {
//            fab.setVisibility(View.GONE);
//        }
    }

    private void canReview(boolean isAccommodation) {
        Call<Boolean> call;
        long guestId = SharedPreferencesManager.getUserInfo(getContext()).getId();

        if (isAccommodation) {
            call = ClientUtils.reviewService.canReviewAccommodation(accommodationId, guestId);
        } else {
            call = ClientUtils.reviewService.canReviewHost(hostIdForCheck, guestId);
        }

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {

                    if (isAccommodation) {
                        canReviewAccommodation = response.body();
                    } else {
                        canReviewHost = response.body();
                    }


                    Log.d("GET Request", "Can review: " + response.body());
                } else {
                    Log.e("GET Request", "Error checking reviewability " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Handle the failure
                t.printStackTrace();
            }
        });

    }

    private void spinnerSetup(Spinner spinner, int optionsResId) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch(position) {
                    case 0:
                        mode = "accommodation";
                        adapter.setMode(mode);
                        customizeFabVisibility(canReviewAccommodation);
                        break;
                    case 1:
                        mode = "host";
                        adapter.setMode(mode);
                        customizeFabVisibility(canReviewHost);
                        break;
                }

                getReviews();

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

    private void getReviews() {
        Call<List<Review>> call;

        if (mode.equals("accommodation")) {
            call = ClientUtils.reviewService.getAccommodationReviews(accommodationId);
        } else {
            call = ClientUtils.reviewService.getHostReviews(hostId);
        }

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


    private void setAdapter(List<Review> reviews) {
        adapter.clear();
        this.adapter.addAll(reviews);
        adapter.notifyDataSetChanged();
    }


}