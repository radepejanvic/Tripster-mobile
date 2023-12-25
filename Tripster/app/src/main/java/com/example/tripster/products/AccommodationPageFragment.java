package com.example.tripster.products;

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
import com.example.tripster.model.view.Product;

import java.util.ArrayList;

public class AccommodationPageFragment extends Fragment {

    public static ArrayList<Product> products = new ArrayList<Product>();
    private AccommodationPageViewModel productsViewModel;
    private FragmentAccommodationPageBinding binding;

    public static AccommodationPageFragment newInstance() {
        return new AccommodationPageFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        productsViewModel = new ViewModelProvider(this).get(AccommodationPageViewModel.class);

        binding = FragmentAccommodationPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        FragmentTransition.to(AccommtionListFragment.newInstance(products), getActivity(), false, R.id.scroll_products_list);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




}