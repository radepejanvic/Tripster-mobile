package com.example.tripster.fragment.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.tripster.adapters.NotificationListAdapter;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentNotificationListBinding;
import com.example.tripster.databinding.FragmentNotificationsBinding;
import com.example.tripster.listener.OnSwipeTouchListener;
import com.example.tripster.model.enums.NotificationStatus;
import com.example.tripster.model.view.Notification;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListFragment extends ListFragment {

    private static final String ARG_PARAM = "param";

    private FragmentNotificationListBinding binding;

    private NotificationListAdapter adapter;

    private NotificationStatus mode;


    public static NotificationListFragment newInstance(ArrayList<Notification> notifications){
        NotificationListFragment fragment = new NotificationListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, notifications);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NotificationListAdapter(getActivity(), new ArrayList<>());
        setListAdapter(adapter);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adapter.clear();

        mode = NotificationStatus.NEW;
        getNotifications();

        binding.list.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                if (mode == NotificationStatus.NEW) {
                    getNotifications();
                }
            }

            public void onSwipeLeft() {
                if (mode == NotificationStatus.READ) {
                    getNotifications();
                }
            }
        });

        return root;
    }

    private void getNotifications() {

        Long userId = SharedPreferencesManager.getUserInfo(getContext()).getUserID();

        Call<List<Notification>> call = null;

        switch(mode) {
            case NEW:
                call = ClientUtils.notificationService.getUnreadNotifications(userId);
                mode = NotificationStatus.READ;
                break;
            case READ:
                call = ClientUtils.notificationService.getReadNotifications(userId);
                mode = NotificationStatus.NEW;
                break;
        }

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if(response.code() == 200) {

                    setAdapter(response.body());

                    Log.d("GET Request", "Notifications count: " + response.body().size());
                } else {
                    Log.e("GET Request", "Error fetching notifications " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setAdapter(List<Notification> reviews) {
        adapter.clear();
        this.adapter.addAll(reviews);
        adapter.notifyDataSetChanged();
    }

}