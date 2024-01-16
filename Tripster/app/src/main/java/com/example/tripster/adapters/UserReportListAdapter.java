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
import com.example.tripster.model.view.Review;
import com.example.tripster.model.view.UserReport;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserReportListAdapter extends ArrayAdapter<UserReport> {

    private ArrayList<UserReport> userReports;

    private UserType userType;

    public UserReportListAdapter(Context context, ArrayList<UserReport> userReports){
        super(context, R.layout.user_report, userReports);
        this.userReports = userReports;
        userType = SharedPreferencesManager.getUserInfo(getContext()).getUserType();
    }

    @Override
    public int getCount() {
        return userReports.size();
    }

    @Nullable
    @Override
    public UserReport getItem(int position) {
        return userReports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserReport userReport = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_report,
                    parent, false);
        }

        TextView reporter = convertView.findViewById(R.id.userReporterName);
        TextView reported = convertView.findViewById(R.id.userReportedName);
        TextView userReportReason = convertView.findViewById(R.id.userReportReasonText);
        TextView delete = convertView.findViewById(R.id.deleteUserReport);
        TextView approve = convertView.findViewById(R.id.approveUserReport);



        if (userReport != null) {
            reporter.setText(userReport.getReporterEmail());
            reported.setText(userReport.getReporteeEmail());
            userReportReason.setText(userReport.getReason());
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<String> call =  ClientUtils.userReportService.deleteReport(userReport.getId());
                userReports.remove(position);
                notifyDataSetChanged();
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("REZ","greska");
                    }
                });

            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<String> call =  ClientUtils.userReportService.approveReport(userReport.getId());
                userReports.remove(position);
                notifyDataSetChanged();
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("REZ","greska");
                    }
                });
            }
        });


        return convertView;
    }


}
