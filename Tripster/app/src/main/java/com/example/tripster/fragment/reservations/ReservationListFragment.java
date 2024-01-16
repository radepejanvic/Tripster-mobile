package com.example.tripster.fragment.reservations;

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
import com.example.tripster.adapters.ReservationListAdapter;
import com.example.tripster.adapters.ReservationListAdapter;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentReservationListBinding;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.model.view.Reservation;
import com.example.tripster.model.view.Reservation;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReservationListFragment extends ListFragment {

    private static final String ARG_PARAM = "param";

    private FragmentReservationListBinding binding;

    private ReservationListAdapter adapter;


    public static ReservationListFragment newInstance(ArrayList<Reservation> Reservations){
        ReservationListFragment fragment = new ReservationListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, Reservations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adapter = new ReservationListAdapter(getActivity(), new ArrayList<>(),this);
            setListAdapter(adapter);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("Tripster", "onCreateView: ReservationsFragment, bundle: " + getArguments());

        binding = FragmentReservationListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (SharedPreferencesManager.getUserInfo(getContext()).getUserType().equals(UserType.HOST)){
            getReservationsForHost();

        }else {
            getReservationsForGuest();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getReservationsForHost( ) {
        Call<List<Reservation>> call = ClientUtils.reservationService.getReservationForHost(SharedPreferencesManager.getUserInfo(getContext()).getId());

        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {

                if(response.code() == 200) {
                    setAdapter(response.body());
                } else {
                    Log.e("GET Request", "Error fetching user " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {

            }
        });
    }
    public void getReservationsForGuest( ) {
        Call<List<Reservation>> call = ClientUtils.reservationService.getReservationForGuest(SharedPreferencesManager.getUserInfo(getContext()).getId());

        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {

                if(response.code() == 200) {
                    setAdapter(response.body());
                } else {
                    Log.e("GET Request", "Error fetching user " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {

            }
        });
    }


    private void setAdapter(List<Reservation> reservations) {
        this.adapter.addAll(reservations);
    }
}