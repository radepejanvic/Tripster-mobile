package com.example.tripster.fragment.reviews;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;

import android.media.Rating;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tripster.R;
import com.example.tripster.adapters.ReviewListAdapter;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentReviewListBinding;
import com.example.tripster.databinding.FragmentReviewsBinding;
import com.example.tripster.model.Accommodation;
import com.example.tripster.model.enums.Mode;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.model.view.Product;
import com.example.tripster.model.view.Review;
import com.example.tripster.products.AccommtionListFragment;
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

    private LayoutInflater layoutInflater;

    private PopupWindow popupWindow;

    private String mode;

    private EditText reviewTitle;

    private EditText reviewComment;

    private RatingBar reviewRate;

    private Review review;

    private FloatingActionButton fab;

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
        }

        adapter.clear();

        spinnerSetup(binding.type, R.array.review_types);

        fab = binding.fab;

        fab.setOnClickListener(v -> {
            layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup popupContainer = (ViewGroup) layoutInflater.inflate(R.layout.review_form, null);
            popupWindow = new PopupWindow(popupContainer, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAtLocation(binding.constraintLayout, Gravity.CENTER, 500, 500);

            popupContainer.setOnTouchListener((v1, event) -> {
                popupWindow.dismiss();
                return true;
            });

            reviewTitle = popupContainer.findViewById(R.id.title_input);
            reviewComment = popupContainer.findViewById(R.id.comment_input);
            reviewRate = popupContainer.findViewById(R.id.rate_input);

            popupContainer.findViewById(R.id.add_review).setOnClickListener(v12 -> {
                if(validateReviewForm()) {
                    loadReviewFromInputs();
                    addReview();
                    popupWindow.dismiss();
                } else {
                    Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                }
            });

        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void customizeFabVisibility() {
        UserType userType = SharedPreferencesManager.getUserInfo(getContext()).getUserType();
//        if (userType != UserType.GUEST || !canAddReview()) {
//
//        }
    }

    private void spinnerSetup(Spinner spinner, int optionsResId) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch(position) {
                    case 0:
                        mode = "accommodation";
                        adapter.setMode(mode);
                        break;
                    case 1:
                        mode = "host";
                        adapter.setMode(mode);
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
                return null;
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

    private boolean validateReviewForm() {
        return  !reviewTitle.getText().toString().isEmpty() &&
                !reviewComment.getText().toString().isEmpty() &&
                reviewRate.getRating() != 0;
    }

    private void loadReviewFromInputs() {
        review = new Review();
        review.setTitle(reviewTitle.getText().toString());
        review.setComment(reviewComment.getText().toString());
        review.setRate(reviewRate.getRating());
        review.setReviewerId((SharedPreferencesManager.getUserInfo(getContext()).getId()));
        Long reviewedId = mode.equals("accommodation") ? accommodationId : hostId;
        review.setReviewedId(reviewedId);
    }

    private void addReview() {

        Call<Review> call;

        if (mode.equals("accommodation")) {
            call = ClientUtils.reviewService.addAccommodationReview(review);
        } else {
            call = ClientUtils.reviewService.addHostReview(review);
        }

        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.code() == 201){
                    Toast.makeText(getContext(), "Sent review", Toast.LENGTH_SHORT).show();
                    Log.d("POST Request", "Review: " + response.body());
                } else {
//                    Toast.makeText(getContext(), "Error review", Toast.LENGTH_SHORT).show();
                    Log.d("POST Request", "Error posting new review " + response.body());
                }
                return null;
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {

            }
        });
    }


}