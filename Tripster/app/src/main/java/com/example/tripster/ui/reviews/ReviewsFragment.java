package com.example.tripster.ui.reviews;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tripster.R;
import com.example.tripster.databinding.FragmentAccountBinding;
import com.example.tripster.databinding.FragmentReviewsBinding;
import com.example.tripster.ui.account.AccountViewModel;

public class ReviewsFragment extends Fragment {

    private FragmentReviewsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReviewsViewModel reviewsViewModel =
                new ViewModelProvider(this).get(ReviewsViewModel.class);

        binding = FragmentReviewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textReviews;
        reviewsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}