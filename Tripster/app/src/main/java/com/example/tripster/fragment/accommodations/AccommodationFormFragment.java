package com.example.tripster.fragment.accommodations;

import static androidx.navigation.ViewKt.findNavController;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
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

import com.example.tripster.R;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentAccommodationFormBinding;
import com.example.tripster.model.Accommodation;
import com.example.tripster.model.enums.AccommodationType;
import com.example.tripster.model.enums.Mode;
import com.example.tripster.util.SharedPreferencesManager;

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
    private EditText number;
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

    private Accommodation accommodation;

    private Long id;

    private Mode mode = Mode.ADD;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            getAccommodation(Long.parseLong(getArguments().getString("id")));
            mode = Mode.UPDATE;
        }

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
        number = binding.number;
        street = binding.street;
        description = binding.description;
        pricePolicy = binding.pricePolicy;
        cancellationPolicy = binding.cancellationPolicy;
        register = binding.register;
        update = binding.update;

        spinnerSetUp(type, R.array.type_options);
        spinnerSetUp1(reservationPolicy, R.array.reservation_policy_options);
        spinnerSetUp2(pricePolicy, R.array.pricing_policy_options);
        spinnerSetUp3(cancellationPolicy, R.array.cancellation_policy_options);

        configureButtons();


        // TODO: Remove this hardcoded call, instead implement pass a variable via navigation which suggests if it is update or create

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateFormNotEmpty()){
                    Toast.makeText(getContext(), "All fields must be filled!", Toast.LENGTH_SHORT).show();
                } else if (validateCapacity()) {
                    Toast.makeText(getContext(), "Minimal capacity must be smaller than maximal!", Toast.LENGTH_SHORT).show();
                }else {
                    accommodation = new Accommodation();
                    accommodation.setOwnerId(SharedPreferencesManager.getUserInfo(getContext()).getId());
                    loadAccommodationFromInputs();
                    postSave();
                    Toast.makeText(getContext(), "Successfully registered " + accommodation.getName() + "!", Toast.LENGTH_SHORT).show();
//                    findNavController(getView()).navigate(R.id.action_navigation_accommodation_form_to_navigation_availability);
                    findNavController(getView()).navigate(R.id.action_navigation_accommodation_form_to_uploadPhotosFragment);
                }

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateFormNotEmpty()){
                    Toast.makeText(getContext(), "All fields must be filled!", Toast.LENGTH_SHORT).show();
                } else if (validateCapacity()) {
                    Toast.makeText(getContext(), "Minimal capacity must be smaller than maximal!", Toast.LENGTH_SHORT).show();
                }else {
                    loadAccommodationFromInputs();
                    updateAccommodation();
                    Toast.makeText(getContext(), "Successfully updated accommodation.", Toast.LENGTH_SHORT).show();
                    findNavController(getView()).navigate(R.id.action_navigation_accommodation_form_to_navigation_availability);
                }


            }
        });

        return root;
    }

    private void configureButtons() {
        switch(mode) {
            case ADD: update.setVisibility(View.GONE); break;
            case UPDATE: register.setVisibility(View.GONE); break;
            default:
                register.setVisibility(View.GONE);
                update.setVisibility(View.GONE);
        }
    }


    private void populateWithAccommodationData() {
        name.setText(accommodation.getName());
        shortDescription.setText(accommodation.getShortDescription());
        minCap.setText(String.valueOf(accommodation.getMinCap()));
        maxCap.setText(String.valueOf(accommodation.getMaxCap()));
        country.setText(accommodation.getCountry());
        city.setText(accommodation.getCity());
        postalCode.setText(accommodation.getZipCode());
        street.setText(accommodation.getStreet());
        number.setText(accommodation.getNumber());
        description.setText(accommodation.getDescription());

        type.setSelection(setType());
        reservationPolicy.setSelection(setReservationPolicy());
        pricePolicy.setSelection(setPricingPolicy());
        cancellationPolicy.setSelection(setCancellationPolicy());
    }

    private void loadAccommodationFromInputs() {

        accommodation.setName(name.getText().toString().trim());
        accommodation.setShortDescription(shortDescription.getText().toString().trim());
        accommodation.setMinCap(Integer.parseInt(minCap.getText().toString().trim()));
        accommodation.setMaxCap(Integer.parseInt(maxCap.getText().toString().trim()));
        accommodation.setCountry(country.getText().toString().trim());
        accommodation.setCity(city.getText().toString().trim());
        accommodation.setZipCode(postalCode.getText().toString().trim());
        accommodation.setNumber(number.getText().toString().trim());
        accommodation.setStreet(street.getText().toString().trim());
        accommodation.setDescription(description.getText().toString().trim());

        accommodation.setCancelDuration(getCancellationPolicy(cancellationPolicyText));
        accommodation.setType(getType(typeText));
        accommodation.setAutomaticReservation(getReservationPolicy(reservationPolicyText));
        accommodation.setPricePerNight(getPricingPolicy(pricePolicyText));
    }

    private boolean validateFormNotEmpty() {
        return  name.getText().toString().isEmpty() ||
                shortDescription.getText().toString().isEmpty() ||
                minCap.getText().toString().isEmpty() ||
                maxCap.getText().toString().isEmpty() ||
                country.getText().toString().isEmpty() ||
                city.getText().toString().isEmpty() ||
                postalCode.getText().toString().isEmpty() ||
                street.getText().toString().isEmpty() ||
                number.getText().toString().isEmpty() ||
                description.getText().toString().isEmpty() ||
                typeText.isEmpty() || reservationPolicyText.isEmpty() ||
                pricePolicyText.isEmpty() || cancellationPolicyText.isEmpty();
    }

    private boolean validateCapacity() {
        return Integer.parseInt(minCap.getText().toString()) > Integer.parseInt(maxCap.getText().toString());
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

    private int setType() {
        switch (accommodation.getType()) {
            case APARTMENT: return 1;
            case STUDIO: return 2;
            case ROOM: return 3;
            default: return -1;
        }
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

    private int setReservationPolicy() {
        return accommodation.isAutomaticReservation() ? 1 : 2;
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

    private int setPricingPolicy() {
        return accommodation.isPricePerNight() ? 1 : 2;
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

    private int setCancellationPolicy() {
        switch (accommodation.getCancelDuration()) {
            case 3: return 1;
            case 7: return 2;
            case 14: return 3;
            case 30: return 4;
            case 0: return 5;
            default: return -1;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void postSave(){
        Call<Accommodation> call = ClientUtils.accommodationService.save(accommodation);

        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.code() == 201){
                    Log.d("POST Request", "Accommodation " + response.body());
                } else {
                    Log.d("POST Request", "Error posting new accommodation " + response.body());
                }

            }

            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {

            }
        });

    }

    private void getAccommodation(long id){
        Call<Accommodation> call = ClientUtils.accommodationService.getAccommodation(id);

        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.code() == 200){
                    accommodation = response.body();
                    populateWithAccommodationData();
                    Log.d("GET Request", "Accommodation " + response.body());
                } else {
                    Log.d("GET Request", "Error fetching accommodation " + response.body());
                }

            }

            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {

            }
        });

    }

    private void updateAccommodation(){
        Call<Accommodation> call = ClientUtils.accommodationService.updateAccommodation(accommodation);

        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.code() == 200){
                    Log.d("PUT Request", "Accommodation " + response.body());
                } else {
                    Log.d("PUT Request", "Error updating accommodation " + response.body());
                }

            }

            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {

            }
        });

    }

}