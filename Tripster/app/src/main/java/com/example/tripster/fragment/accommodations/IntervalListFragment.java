package com.example.tripster.fragment.accommodations;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.ListFragment;

import com.example.tripster.adapters.IntervalListAdapter;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentIntervalListBinding;
import com.example.tripster.model.enums.Mode;
import com.example.tripster.model.view.Interval;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntervalListFragment extends ListFragment {

    private static final String ARG_PARAM = "param";

    private FragmentIntervalListBinding binding;

    private IntervalListAdapter adapter;

    private Long id;

    public static IntervalListFragment newInstance(ArrayList<Interval> intervals) {
        IntervalListFragment fragment = new IntervalListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, intervals);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong("id");
            adapter = new IntervalListAdapter(getActivity(), new ArrayList<>());
            setListAdapter(adapter);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentIntervalListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adapter.clear();
        getIntervals();

        return root;
    }

    public void getIntervals() {

        Call<List<Interval>> call = ClientUtils.availabilityService.getCalendar(id);

        call.enqueue(new Callback<List<Interval>>() {
            @Override
            public void onResponse(Call<List<Interval>> call, Response<List<Interval>> response) {
                if(response.code() == 200) {

                    setAdapter(response.body());
                    Log.d("KKKKKKKK", String.valueOf(adapter.getCount()));


                    Log.d("GET Request", "Interval count: " + response.body().size());
                } else {
                    Log.e("GET Request", "Error fetching calendar " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Interval>> call, Throwable t) {

            }
        });
    }

    private void setAdapter(List<Interval> intervals) {
        adapter.clear();
        this.adapter.addAll(intervals);
        adapter.notifyDataSetChanged();
    }
}