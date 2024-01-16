package com.example.tripster.fragment.users;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripster.R;
import com.example.tripster.adapters.UserReportListAdapter;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentUserReportListBinding;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.model.view.UserReport;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserReportListFragment extends ListFragment {

    private static final String ARG_PARAM = "param";

    private FragmentUserReportListBinding binding;

    private UserReportListAdapter adapter;


    public static UserReportListFragment newInstance(ArrayList<UserReport> UserReports){
        UserReportListFragment fragment = new UserReportListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, UserReports);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adapter = new UserReportListAdapter(getActivity(), new ArrayList<>());
            setListAdapter(adapter);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("Tripster", "onCreateView: UserReportsFragment, bundle: " + getArguments());

        binding = FragmentUserReportListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        getAllUserReport();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getAllUserReport( ) {
        Call<List<UserReport>> call = ClientUtils.userReportService.getAllUserReport();

        call.enqueue(new Callback<List<UserReport>>() {
            @Override
            public void onResponse(Call<List<UserReport>> call, Response<List<UserReport>> response) {

                if(response.code() == 200) {
                    setAdapter(response.body());
                } else {
                    Log.e("GET Request", "Error fetching user " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserReport>> call, Throwable t) {

            }
        });
    }

    private void setAdapter(List<UserReport> UserReports) {
        this.adapter.addAll(UserReports);
    }
}