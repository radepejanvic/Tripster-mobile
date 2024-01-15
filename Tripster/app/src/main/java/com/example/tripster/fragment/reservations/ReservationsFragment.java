package com.example.tripster.fragment.reservations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripster.FragmentTransition;
import com.example.tripster.R;
import com.example.tripster.databinding.FragmentReservationsBinding;
import com.example.tripster.fragment.reviews.ReviewListFragment;
import com.example.tripster.model.view.Reservation;
import com.example.tripster.model.view.Review;

import java.util.ArrayList;

public class ReservationsFragment extends Fragment {

    public static ArrayList<Reservation> reservations = new ArrayList<>();

    private FragmentReservationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentReservationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentTransition.to(ReservationListFragment.newInstance(reservations), getActivity(), false, R.id.scroll_reviews_list);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}