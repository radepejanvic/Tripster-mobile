package com.example.tripster.fragment.reviews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tripster.FragmentTransition;
import com.example.tripster.R;
import com.example.tripster.databinding.FragmentReviewsApprovalBinding;
import com.example.tripster.databinding.FragmentReviewsBinding;
import com.example.tripster.model.view.Review;

import java.util.ArrayList;


public class ReviewsApprovalFragment extends Fragment {

    public static ArrayList<Review> reviews = new ArrayList<>();

    private FragmentReviewsApprovalBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewsApprovalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ReviewApprovalListFragment listFragment = ReviewApprovalListFragment.newInstance(new ArrayList<>());
        spinnerSetup(binding.type, R.array.review_reports);

        FragmentTransition.to(listFragment, getActivity(), false, R.id.scroll_review_report_list);

        return root;
    }
    private void spinnerSetup(Spinner spinner, int optionsResId) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch(position) {
                    case 0:
                        FragmentTransition.to(ReviewApprovalListFragment.newInstance(new ArrayList<>()), getActivity(), false, R.id.scroll_review_report_list);
                        break;
                    case 1:
                        FragmentTransition.to(ReviewReportlListFragment.newInstance(new ArrayList<>()), getActivity(), false, R.id.scroll_review_report_list);
                        break;
                    case 2:
                        FragmentTransition.to(ReviewReportlListFragment.newInstance(new ArrayList<>()), getActivity(), false, R.id.scroll_review_report_list);
                        break;
                }

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
}