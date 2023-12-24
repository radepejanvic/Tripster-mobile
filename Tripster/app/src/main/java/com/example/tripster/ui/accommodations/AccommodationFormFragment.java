package com.example.tripster.ui.accommodations;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.example.tripster.AuthorizationActivity;
import com.example.tripster.MainActivity;
import com.example.tripster.R;
import com.example.tripster.databinding.FragmentAccommodationFormBinding;

import java.util.ArrayList;
import java.util.List;

public class AccommodationFormFragment extends Fragment {

    private static MainActivity ARG_PARAM1 = new MainActivity();

    private FragmentAccommodationFormBinding binding;
    private ScrollView scrollView;
    private EditText name;
    private EditText shortDescription;
    private Spinner type;
    private Spinner reservationPolicy;
    private EditText minCap;
    private EditText maxCap;
    private EditText country;
    private EditText city;
    private EditText postalCode;
    private EditText streetAndNumber;
    private EditText description;
    private Spinner pricePolicy;
    private Spinner cancellationPolicy;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAccommodationFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        scrollView = binding.scroll;
        name = binding.name;
        shortDescription = binding.shortDescription;
        type = binding.type;
        reservationPolicy = binding.reservationPolicy;
        minCap = binding.minCap;
        maxCap = binding.maxCap;
        country = binding.country;
        city = binding.city;
        postalCode = binding.postalCode;
        streetAndNumber = binding.streetAndNumber;
        description = binding.description;
        pricePolicy = binding.pricePolicy;
        cancellationPolicy = binding.cancellationPolicy;

        return root;
    }

    private void spinnerSetUp(Spinner spinner, ArrayList<String> options) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String>adapter = new ArrayAdapter<>(ARG_PARAM1, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);
    }


}