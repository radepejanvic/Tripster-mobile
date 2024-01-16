package com.example.tripster.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tripster.R;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.model.Accommodation;
import com.example.tripster.model.enums.AccommodationStatus;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.model.view.ReviewReport;
import com.example.tripster.model.view.UserReport;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewReportListAdapter extends ArrayAdapter<ReviewReport> {

    private ArrayList<ReviewReport> reviewReports;

    private Long mode;
    private UserType userType;

    public ReviewReportListAdapter(Context context, ArrayList<ReviewReport> reviewReports,Long mode){
        super(context, R.layout.user_report, reviewReports);
        this.mode = mode;
        this.reviewReports = reviewReports;
        userType = SharedPreferencesManager.getUserInfo(getContext()).getUserType();
    }

    @Override
    public int getCount() {
        return reviewReports.size();
    }

    @Nullable
    @Override
    public ReviewReport getItem(int position) {
        return reviewReports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReviewReport reviewReport = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_report,
                    parent, false);
        }

        TextView title = convertView.findViewById(R.id.titleAccommodation);
        TextView reporter = convertView.findViewById(R.id.userReporterName);
        TextView reason = convertView.findViewById(R.id.userReportReasonText);
        TextView comment = convertView.findViewById(R.id.commentText);
        TextView delete = convertView.findViewById(R.id.deleteUserReviewReport);
        TextView approve = convertView.findViewById(R.id.approveUserReviewReport);

        if (reviewReport != null){
            title.setText(reviewReport.getName());
            reporter.setText(reviewReport.getReporterEmail());
            reason.setText(reviewReport.getReason());
            comment.setText(reviewReport.getComment());
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<String> call;
                if (mode == 1){
                    call =  ClientUtils.reviewReportService.deleteAccommodationReviewReports(reviewReport.getId());
                }else {
                    call =  ClientUtils.reviewReportService.deleteUserReviewReports(reviewReport.getId());
                }

                reviewReports.remove(position);
                notifyDataSetChanged();
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<String> call;
                if (mode == 1){
                    call =  ClientUtils.reviewReportService.approveAccommodationReviewReports(reviewReport.getId());
                }else {
                    call =  ClientUtils.reviewReportService.approveUserReviewReports(reviewReport.getId());
                }
                reviewReports.remove(position);
                notifyDataSetChanged();
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

        return convertView;
    }
}
