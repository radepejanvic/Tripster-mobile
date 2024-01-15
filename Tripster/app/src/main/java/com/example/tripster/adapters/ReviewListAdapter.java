package com.example.tripster.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tripster.R;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.model.User;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.model.view.Review;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;

import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Setter
public class ReviewListAdapter extends ArrayAdapter<Review> {

    private ArrayList<Review> reviews;

    private UserType userType;

    private String mode;

    public ReviewListAdapter(Context context, ArrayList<Review> reviews){
        super(context, R.layout.review_card, reviews);
        this.reviews = reviews;
        userType = SharedPreferencesManager.getUserInfo(getContext()).getUserType();
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Nullable
    @Override
    public Review getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Review review = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_card,
                    parent, false);
        }

        ConstraintLayout card = convertView.findViewById(R.id.review_card);
        TextView title = convertView.findViewById(R.id.review_title);
        TextView reviewer = convertView.findViewById(R.id.review_reviewer);
        TextView timeStamp = convertView.findViewById(R.id.review_timestamp);
        TextView rate = convertView.findViewById(R.id.review_rate);
        TextView comment = convertView.findViewById(R.id.review_comment);
        ImageView delete = convertView.findViewById(R.id.delete_review);
        ImageView report = convertView.findViewById(R.id.report_review);

        customizeImageViewVisibility(delete, report, review.getReviewerId());

        if (review != null) {
            title.setText(review.getTitle());
            reviewer.setText(review.getReviewerName() + " " + review.getReviewerSurname());
            timeStamp.setText(review.getTimeStamp());
            rate.setText(String.valueOf(review.getRate()));
            comment.setText(review.getComment());
        }

        delete.setOnClickListener(v -> {
            deleteReview(review.getId(), position);
        });

        report.setOnClickListener(v -> {
            // TODO: Report review
        });

        return convertView;
    }

    private void customizeImageViewVisibility(ImageView delete, ImageView report, Long reviewerId) {
        switch(userType) {
            case HOST:
                delete.setVisibility(View.GONE);
                break;
            case GUEST:
                report.setVisibility(View.GONE);
                if (SharedPreferencesManager.getUserInfo(getContext()).getUserID() != reviewerId) {
                    delete.setVisibility(View.GONE);
                }
                break;
            case ADMIN:
                delete.setVisibility(View.GONE);
                report.setVisibility(View.GONE);
        }
    }

    private void deleteReview(Long reviewId, int position){
        Call<Boolean> call;

        if (mode.equals("accommodation")) {
            call = ClientUtils.reviewService.deleteAccommodationReview(reviewId);
        } else {
            call = ClientUtils.reviewService.deleteHostReview(reviewId);
        }

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if(response.code() == 200) {
                    reviews.remove(position);
                    notifyDataSetChanged();

                    Toast.makeText(getContext(), "Deleted review", Toast.LENGTH_SHORT).show();
                    Log.d("DELETE Request", "Successfully deleted review ");

                } else {
                   Log.e("DELETE Request", "Error deleting review " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("DELETE Request", "Failure REVIEW");
            }
        });
    }

}
