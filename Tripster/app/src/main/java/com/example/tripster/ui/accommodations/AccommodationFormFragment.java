package com.example.tripster.ui.accommodations;

import static androidx.navigation.ViewKt.findNavController;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
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
import android.widget.TextView;

import com.example.tripster.AuthorizationActivity;
import com.example.tripster.MainActivity;
import com.example.tripster.R;
import com.example.tripster.databinding.FragmentAccommodationFormBinding;
import com.example.tripster.ui.account.AccountViewModel;

import java.util.ArrayList;
import java.util.List;

public class AccommodationFormFragment extends Fragment {

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
    private EditText streetNumber;
    private EditText street;
    private EditText description;
    private Spinner pricePolicy;
    private Spinner cancellationPolicy;

    private Button register;
    private Button update;
    private Button delete;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AccommodationFormViewModel accommodationFormViewModel =
                new ViewModelProvider(this).get(AccommodationFormViewModel.class);

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
        streetNumber = binding.streetNumber;
        street = binding.street;
        description = binding.description;
        pricePolicy = binding.pricePolicy;
        cancellationPolicy = binding.cancellationPolicy;
        register = binding.register;
        update = binding.update;
        delete = binding.delete;

        spinnerSetUp(type, R.array.type_options);
        spinnerSetUp(reservationPolicy, R.array.reservation_policy_options);
        spinnerSetUp(pricePolicy, R.array.pricing_policy_options);
        spinnerSetUp(cancellationPolicy, R.array.cancellation_policy_options);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(v).navigate(R.id.action_navigation_accommodation_form_to_navigation_availability);
            }
        });


        return root;
    }

    private void spinnerSetUp(Spinner spinner, int optionsResId) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle item selection
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (position == 0) {
                    // Handle the case where the first item is selected
                } else {
                    // Handle other selections
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
            }
        });

        String[] options = getResources().getStringArray(optionsResId);

        // Create a custom adapter to disable the first item
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                spinner.getContext(),
                android.R.layout.simple_spinner_item,
                options
        ) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // Disable the first item
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    // Disable the first item in the dropdown
                    view.setEnabled(false);
                    TextView textView = (TextView) view;
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.hint)); // Optionally change the color of disabled item
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}