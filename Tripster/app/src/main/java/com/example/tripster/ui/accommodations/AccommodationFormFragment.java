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
import android.widget.Toast;

import com.example.tripster.AuthorizationActivity;
import com.example.tripster.MainActivity;
import com.example.tripster.R;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentAccommodationFormBinding;
import com.example.tripster.model.Accommodation;
import com.example.tripster.model.enums.AccommodationType;
import com.example.tripster.ui.account.AccountViewModel;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private String typeText;
    private String reservationPolicyText;
    private String pricePolicyText;
    private String cancellationPolicyText;

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
        spinnerSetUp1(reservationPolicy, R.array.reservation_policy_options);
        spinnerSetUp2(pricePolicy, R.array.pricing_policy_options);
        spinnerSetUp3(cancellationPolicy, R.array.cancellation_policy_options);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText =name.getText().toString().trim();
                String shortDescriptionText = shortDescription.getText().toString().trim();
                String minCapText = minCap.getText().toString().trim();
                String maxCapText = maxCap.getText().toString().trim();
                String countryText = country.getText().toString().trim();
                String cityText = city.getText().toString().trim();
                String postalCodeText = postalCode.getText().toString().trim();
                String streetNumberText = streetNumber.getText().toString().trim();
                String streetText = street.getText().toString().trim();
                String descriptionText = description.getText().toString().trim();

                if (nameText.isEmpty() || shortDescriptionText.isEmpty() || minCapText.isEmpty() || maxCapText.isEmpty() ||
                countryText.isEmpty()|| cityText.isEmpty() || postalCodeText.isEmpty() || streetText.isEmpty()||
                streetNumberText.isEmpty() || descriptionText.isEmpty() | typeText.isEmpty() || reservationPolicyText.isEmpty() ||
                pricePolicyText.isEmpty() || cancellationPolicyText.isEmpty()){
                    Toast.makeText(getContext(), "All fields must be fill!", Toast.LENGTH_SHORT).show();

                } else if (Integer.parseInt(minCapText) > Integer.parseInt(maxCapText)) {
                    Toast.makeText(getContext(), "Min capacity can't bigger then max capacity!", Toast.LENGTH_SHORT).show();
                }else {
                    Accommodation accommodation = new Accommodation(nameText, SharedPreferencesManager.getUserInfo(getContext()).getPersonID(),countryText,cityText,
                            postalCodeText,streetText,streetNumberText,shortDescriptionText,descriptionText,Integer.parseInt(minCapText),Integer.parseInt(maxCapText),
                            getCancellationPolicy(cancellationPolicyText),getType(typeText),getReservationPolicy(reservationPolicyText),getPricingPolicy(pricePolicyText));
                    postSave(accommodation);
                }


            }
        });


        return root;
    }

    private void spinnerSetUp(Spinner spinner, int optionsResId) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    typeText ="";
                } else {
                    typeText = (String) parent.getItemAtPosition(position);
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

    private void spinnerSetUp1(Spinner spinner, int optionsResId) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    reservationPolicyText = "";
                } else {
                    reservationPolicyText = (String) parent.getItemAtPosition(position);
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

    private void spinnerSetUp2(Spinner spinner, int optionsResId) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    pricePolicyText ="";
                } else {
                    pricePolicyText = (String) parent.getItemAtPosition(position);
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

    private void spinnerSetUp3(Spinner spinner, int optionsResId) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    cancellationPolicyText = "";
                } else {
                    cancellationPolicyText = (String) parent.getItemAtPosition(position);
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

    private AccommodationType getType(String type){
        switch (type){
            case  "Apartment":
                return AccommodationType.APARTMENT;
            case  "Studio":
                return AccommodationType.STUDIO;
            case  "Room":
                return AccommodationType.ROOM;
        }
        return null;
    }

    private Boolean getReservationPolicy(String type){
        switch (type){
            case  "Automatic approval (advised)":
                return true;
            case  "Manual approval":
                return false;
        }
        return null;
    }

    private Boolean getPricingPolicy(String type){
        switch (type){
            case  "Per night (advised)":
                return true;
            case  "Per guest per night":
                return false;
        }
        return null;
    }

    private int getCancellationPolicy(String type){
        switch (type){
            case  "3 days before":
                return 3;
            case  "7 days before":
                return 7;
            case  "14 days before":
                return 14;
            case  "1 month before":
                return 30;
            case  "None":
                return 0;
        }
        return 0;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void postSave(Accommodation accommodation){
        Call<Accommodation> call = ClientUtils.accommodationService.save(accommodation);

        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.code() == 201){
                    findNavController(getView()).navigate(R.id.action_navigation_accommodation_form_to_navigation_availability);
                }
            }

            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {

            }
        });

    }

}