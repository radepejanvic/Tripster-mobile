package com.example.tripster.ui.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.tripster.databinding.FragmentAccountBinding;
import com.example.tripster.ui.DummyUser;

public class AccountFragment extends Fragment {


    private FragmentAccountBinding binding;

    private ScrollView scrollView;
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText phone;
    private EditText country;
    private EditText city;
    private EditText postalCode;
    private EditText streetAndNumber;
    private boolean changePassVisible = false;
    private EditText currentPass;
    private EditText newPass;
    private EditText repeatNewPass;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loadUserData();

        scrollView = binding.scroll;
        name = binding.name;
        surname = binding.surname;
        email = binding.email;
        phone = binding.phone;
        country = binding.country;
        city = binding.city;
        postalCode = binding.postalCode;
        streetAndNumber = binding.streetAndNumber;
        currentPass = binding.currentPassword;
        newPass = binding.newPassword;
        repeatNewPass = binding.repeatNew;

        hideChangePass();
        alterFieldEditable(false);

        binding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changePassVisible) {
                    hideChangePass();
                } else {
                    showChangePass();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
                changePassVisible = !changePassVisible;
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void hideChangePass() {
        currentPass.setVisibility(View.GONE);
        newPass.setVisibility(View.GONE);
        repeatNewPass.setVisibility(View.GONE);
    }

    private void showChangePass() {
        currentPass.setVisibility(View.VISIBLE);
        newPass.setVisibility(View.VISIBLE);
        repeatNewPass.setVisibility(View.VISIBLE);
    }

    // TODO: Delete this when the connection with backend is made.
    private void loadUserData() {
        DummyUser user = new DummyUser();
        binding.name.setText(user.name);
        binding.surname.setText(user.surname);
        binding.email.setText(user.email);
        binding.phone.setText(user.phone);
        binding.country.setText(user.country);
        binding.city.setText(user.city);
        binding.postalCode.setText(user.postalCode);
        binding.streetAndNumber.setText(user.streetAndNumber);
    }

    private void alterFieldEditable(boolean editable) {
        name.setFocusable(editable);
        name.setFocusableInTouchMode(editable);
        surname.setFocusable(editable);
        surname.setFocusableInTouchMode(editable);
        email.setFocusable(editable);
        email.setFocusableInTouchMode(editable);
        phone.setFocusableInTouchMode(editable);
        phone.setFocusableInTouchMode(editable);
        country.setFocusableInTouchMode(editable);
        country.setFocusableInTouchMode(editable);
        city.setFocusableInTouchMode(editable);
        city.setFocusableInTouchMode(editable);
        postalCode.setFocusableInTouchMode(editable);
        postalCode.setFocusableInTouchMode(editable);
        streetAndNumber.setFocusableInTouchMode(editable);
        streetAndNumber.setFocusableInTouchMode(editable);
    }
}