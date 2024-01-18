package com.example.tripster.fragment.accommodations;

import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

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
import com.example.tripster.databinding.FragmentAvailabilityBinding;

import com.example.tripster.model.enums.Mode;
import com.example.tripster.model.view.Interval;
import com.example.tripster.util.DateTimeUtil;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvailabilityFragment extends Fragment {

    private FragmentAvailabilityBinding binding;
    private ScrollView scrollView;

    private Long id;

    private Mode mode;

    private Spinner spinner;
    private EditText price;
    private Button add;
    private Button update;
    private Button disable;
    private Button calendar;

    private MaterialDatePicker<Pair<Long, Long>> dateRangePicker;

    private List<Interval> currentIntervals;

    private List<Interval> intervals;

    private LocalDate start;

    private LocalDate end;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong("id");
            mode = Mode.valueOf(getArguments().getString("mode"));
        }
        intervals = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAvailabilityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        scrollView = binding.scroll;
        price = binding.price;
        spinner = binding.mode;
        add = binding.addPriceList;
        update = binding.updatePriceList;
        disable = binding.removePriceList;
        calendar = binding.calendar;

        spinnerSetUp(spinner, R.array.availability_mode_options);

        initDateRangePicker();
        calendar.setOnClickListener(v -> showDateRangePicker());

        add.setOnClickListener(v -> {
            if (validateDateRange() && validatePrice()) {
                intervals.add(loadIntervalFromInputs());
                addCalendar();
            }
        });

        update.setOnClickListener(v -> {

        });

        disable.setOnClickListener(v -> {

        });

        return root;
    }

    private void initDateRangePicker() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        dateRangePicker = builder.build();

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        DateValidatorPointForward dateValidator = DateValidatorPointForward.now();
        constraintsBuilder.setValidator(dateValidator);

        builder.setCalendarConstraints(constraintsBuilder.build());
        dateRangePicker = builder.build();

        dateRangePicker.addOnPositiveButtonClickListener(selection -> {
            start = DateTimeUtil.toLocalDate(selection.first);
            end = DateTimeUtil.toLocalDate(selection.second);
        });
    }

    private void showDateRangePicker() {
        dateRangePicker.show(requireActivity().getSupportFragmentManager(), dateRangePicker.toString());
    }

    private boolean validateDateRange() {
        return !start.equals(null) &&
                !end.equals(null);
    }

    private boolean validatePrice() {
        return !price.getText().toString().isEmpty() &&
                Double.parseDouble(price.getText().toString()) > 0;
    }

    private Interval loadIntervalFromInputs() {
        return new Interval(start, end, Double.parseDouble(price.getText().toString()));
    }

    private void clearForm() {
        start = null;
        end = null;
        price.setText("");
        intervals.clear();
    }

    private void addCalendar() {
        Call<Integer> call = ClientUtils.availabilityService.addCalendar(id, intervals);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200) {

                    clearForm();
                    switchMode();
                    Toast.makeText(getContext(), "Added initial price list", Toast.LENGTH_SHORT).show();
                    Log.d("POST Request", "Added " + response.body() + " days");
                } else {
                    Log.e("POST Request", "Error adding calendar " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void switchMode() {
        mode = Mode.UPDATE;
        add.setVisibility(View.GONE);
        update.setVisibility(View.VISIBLE);
    }


    private void spinnerSetUp(Spinner spinner, int optionsResId) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        add.setVisibility(View.GONE);
                        update.setVisibility(View.GONE);
                        price.setVisibility(View.GONE);
                        disable.setVisibility(View.GONE);
                        break;
                    case 1:
                        add.setVisibility(mode == Mode.ADD ? View.VISIBLE : View.GONE);
                        update.setVisibility(mode == Mode.UPDATE ? View.VISIBLE : View.GONE);
                        price.setVisibility(View.VISIBLE);
                        disable.setVisibility(View.GONE);
                        break;
                    case 2:
                        add.setVisibility(View.GONE);
                        update.setVisibility(View.GONE);
                        price.setVisibility(View.GONE);
                        disable.setVisibility(mode == Mode.UPDATE ? View.VISIBLE : View.GONE);
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