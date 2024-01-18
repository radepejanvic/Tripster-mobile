package com.example.tripster.fragment.accommodations;

import androidx.core.content.ContextCompat;
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
import android.widget.TextView;

import com.example.tripster.R;
import com.example.tripster.databinding.FragmentAccommodationFormBinding;
import com.example.tripster.databinding.FragmentAvailabilityBinding;

public class AvailabilityFragment extends Fragment {

    private FragmentAvailabilityBinding binding;
    private ScrollView scrollView;
    private Spinner mode;
    private EditText price;
    private Button add;
    private Button disable;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAvailabilityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        scrollView = binding.scroll;
        price = binding.price;
        mode = binding.mode;
        add = binding.addPriceList;
        disable = binding.removePriceList;

        spinnerSetUp(mode, R.array.availability_mode_options);

        return root;
    }

    private void spinnerSetUp(Spinner spinner, int optionsResId) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        add.setVisibility(View.GONE);
                        price.setVisibility(View.GONE);
                        disable.setVisibility(View.GONE);
                        break;
                    case 1:
                        add.setVisibility(View.VISIBLE);
                        price.setVisibility(View.VISIBLE);
                        disable.setVisibility(View.GONE);
                        break;
                    case 2:
                        add.setVisibility(View.GONE);
                        price.setVisibility(View.GONE);
                        disable.setVisibility(View.VISIBLE);
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