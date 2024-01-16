package com.example.tripster.fragment.reports;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.DialogReportBinding;
import com.example.tripster.model.Report;
import com.example.tripster.model.enums.ReportType;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDialog extends Dialog {

    private DialogReportBinding binding;

    private Long reporterId;

    private Long reporteeId;

    private EditText reason;

    private ReportType type;

    public ReportDialog(@NonNull Context context, Long reporterId, Long reporteeId, ReportType type) {
        super(context);
        this.reporterId = reporterId;
        this.reporteeId = reporteeId;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = DialogReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reason = binding.reasonInput;

        getWindow().setContentView(binding.getRoot(),new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        binding.addReport.setOnClickListener(v -> {

            if(validateReportForm()) {
                addReport(loadReportFromInputs());
            } else {
                Toast.makeText(getContext(), "Failed to send report", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void addReport(Report report) {
        Call<Report> call = null;

        switch(type) {
            case USER:  call = ClientUtils.reportService.reportUser(report); break;
            case HOST_REVIEW:  call = ClientUtils.reportService.reportHostReview(report); break;
            case ACCOMMODATION_REVIEW: call = ClientUtils.reportService.reportAccommodationReview(report); break;
        }

        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                if (response.isSuccessful()) {
                    dismiss();
                    Toast.makeText(getContext(), "Report sent", Toast.LENGTH_SHORT).show();
                    Log.d("POST Request", "Report: " + response.body());
                } else {
                    Log.e("POST Request", "Error reporting " + type + " " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private boolean validateReportForm() {
        return  !reason.getText().toString().isEmpty() &&
                reporterId > 0 &&
                reporteeId > 0;
    }

    private Report loadReportFromInputs() {
        Report report = new Report();
        report.setReporterId(reporterId);
        report.setReason(reason.getText().toString());

        switch (type) {
            case USER: report.setReporteeId(reporteeId); break;
            default: report.setReviewId(reporteeId);
        }

        return report;
    }

}
