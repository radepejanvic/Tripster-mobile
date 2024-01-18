package com.example.tripster.fragment.settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.Switch;
import android.widget.Toast;

import com.example.tripster.R;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentAccountBinding;
import com.example.tripster.databinding.FragmentSettingsBinding;
import com.example.tripster.model.Settings;
import com.example.tripster.model.User;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.util.SharedPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private UserType userType;

    private Switch reservation;
    private Switch review;
    private Switch accommodationReview;

    private Button apply;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//           TODO: Maybe will need something
        }
        userType = SharedPreferencesManager.getUserInfo(getContext()).getUserType();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        reservation = binding.reservationNotifications;
        review = binding.reviewNotifications;
        accommodationReview = binding.accommodationReviewNotifications;
        apply = binding.button;

        loadSettings();
        customizeSwitches();

        apply.setOnClickListener(v -> {
            applySettings();
        });


        return root;
    }

    private void loadSettings() {
        Settings settings = SharedPreferencesManager.getSettings(getContext());
        reservation.setChecked(settings.isReservationNotification());
        review.setChecked(settings.isReviewNotification());
        accommodationReview.setChecked(settings.isAccommodationReviewNotification());
    }

    private Settings loadSettingsFromInputs() {
        Settings settings = SharedPreferencesManager.getSettings(getContext());
        settings.setReservationNotification(reservation.isChecked());
        settings.setReviewNotification(review.isChecked());
        settings.setAccommodationReviewNotification(accommodationReview.isChecked());
        return settings;
    }

    private void customizeSwitches() {
        if (userType != UserType.HOST) {
            review.setClickable(false);
            review.setChecked(false);
            review.setAlpha(0.5F);
            accommodationReview.setClickable(false);
            accommodationReview.setChecked(false);
            accommodationReview.setAlpha(0.5F);
        }

        if (userType == UserType.ADMIN) {
            reservation.setClickable(false);
            reservation.setChecked(false);
            reservation.setAlpha(0.5F);
            apply.setVisibility(View.GONE);
        }
    }

    private void applySettings(){
        Call<Settings> call = ClientUtils.settingsService.changeSettings(loadSettingsFromInputs());

        call.enqueue(new Callback<Settings>() {
            @Override
            public void onResponse(Call<Settings> call, Response<Settings> response) {
                if(response.code() == 200) {
                    SharedPreferencesManager.saveSettings(getContext(), response.body());
                    Toast.makeText(getContext(), "Saved settings", Toast.LENGTH_SHORT).show();
                    Log.d("PUT Request", "Settings " + response.body());
                } else {
                    Log.e("PUT Request", "Error updating user " + response.code());
                }

            }

            @Override
            public void onFailure(Call<Settings> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}