package com.example.tripster.fragment.reviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.DialogReportBinding;
import com.example.tripster.databinding.DialogReviewBinding;
import com.example.tripster.model.Report;
import com.example.tripster.model.enums.ReportType;
import com.example.tripster.model.enums.ReviewType;
import com.example.tripster.model.view.Review;
import com.example.tripster.util.SharedPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewDialog extends Dialog {

    private DialogReviewBinding binding;

    private Long reviewerId;

    private Long reviewedId;

    private EditText reviewTitle;

    private EditText reviewComment;

    private RatingBar reviewRate;

    private ReviewType type;

    public ReviewDialog(@NonNull Context context, Long reviewerId, Long reviewedId, ReviewType type) {
        super(context);
        this.reviewerId = reviewerId;
        this.reviewedId = reviewedId;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = DialogReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reviewTitle = binding.titleInput;
        reviewComment = binding.commentInput;
        reviewRate = binding.rateInput;

        getWindow().setContentView(binding.getRoot(),new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        binding.addReview.setOnClickListener(v -> {

            if(validateReviewForm()) {
                addReview(loadReviewFromInputs());
            } else {
                Toast.makeText(getContext(), "Failed to send review", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void addReview(Review review) {

        Call<Review> call = null;

        switch(type) {
            case HOST:  call = ClientUtils.reviewService.addHostReview(review); break;
            case ACCOMMODATION:  call = ClientUtils.reviewService.addAccommodationReview(review);
        }

        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.code() == 201){
                    dismiss();
                    Toast.makeText(getContext(), "Review sent", Toast.LENGTH_SHORT).show();
                    Log.d("POST Request", "Review: " + response.body());
                } else {
//                    Toast.makeText(getContext(), "Error review", Toast.LENGTH_SHORT).show();
                    Log.d("POST Request", "Error posting new review " + response.body());
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {

            }
        });
    }

    private boolean validateReviewForm() {
        return  !reviewTitle.getText().toString().isEmpty() &&
                !reviewComment.getText().toString().isEmpty() &&
                reviewRate.getRating() != 0;
    }

    private Review loadReviewFromInputs() {
        Review review = new Review();
        review.setTitle(reviewTitle.getText().toString());
        review.setComment(reviewComment.getText().toString());
        review.setRate((int)reviewRate.getRating());
        review.setReviewerId((SharedPreferencesManager.getUserInfo(getContext()).getId()));
        review.setReviewedId(reviewedId);
        return review;
    }

}
