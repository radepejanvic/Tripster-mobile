package com.example.tripster.fragment.users;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tripster.FragmentTransition;
import com.example.tripster.R;
import com.example.tripster.databinding.FragmentUsersBinding;
import com.example.tripster.fragment.reservations.ReservationListFragment;
import com.example.tripster.model.view.Reservation;
import com.example.tripster.model.view.UserReport;

import java.util.ArrayList;

public class UsersFragment extends Fragment {

    public static ArrayList<UserReport> userReports = new ArrayList<>();

    private FragmentUsersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentTransition.to(UserReportListFragment.newInstance(userReports), getActivity(), false, R.id.scroll_user_report_list);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}