package com.example.tripster.fragment.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tripster.FragmentTransition;
import com.example.tripster.R;
import com.example.tripster.databinding.FragmentNotificationsBinding;
import com.example.tripster.model.view.Notification;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    public static ArrayList<Notification> notifications = new ArrayList<>();

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentTransition.to(NotificationListFragment.newInstance(notifications), getActivity(), false, R.id.scroll_notifications_list);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}