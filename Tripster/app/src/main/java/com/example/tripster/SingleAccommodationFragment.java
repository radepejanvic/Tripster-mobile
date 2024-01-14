package com.example.tripster;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tripster.databinding.FragmentAccountBinding;
import com.example.tripster.databinding.FragmentSingleAccommodationBinding;
import com.example.tripster.util.SharedPreferencesManager;

public class SingleAccommodationFragment extends Fragment {

    private FragmentSingleAccommodationBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSingleAccommodationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.reviews.setOnClickListener(v -> {
            findNavController(v).navigate(R.id.action_singleAccommodationFragment_to_reviewFragment);
        });


        return root;
    }
}