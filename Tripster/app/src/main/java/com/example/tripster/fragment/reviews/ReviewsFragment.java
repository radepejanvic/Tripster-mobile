package com.example.tripster.fragment.reviews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripster.FragmentTransition;
import com.example.tripster.R;
import com.example.tripster.databinding.FragmentAccommodationPageBinding;
import com.example.tripster.databinding.FragmentReviewsBinding;
import com.example.tripster.fragment.reviews.ReviewListFragment;
import com.example.tripster.model.view.Product;
import com.example.tripster.model.view.Review;
import com.example.tripster.products.AccommodationPageFragment;
import com.example.tripster.products.AccommodationPageViewModel;
import com.example.tripster.products.AccommtionListFragment;

import java.util.ArrayList;

public class ReviewsFragment extends Fragment {

    public static ArrayList<Review> reviews = new ArrayList<>();

    private FragmentReviewsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentTransition.to(ReviewListFragment.newInstance(reviews), getActivity(), false, R.id.scroll_reviews_list);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}