package com.example.tripster.fragment.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.tripster.MainActivity;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentAccountBinding;
import com.example.tripster.model.User;
import com.example.tripster.util.SharedPreferencesManager;
import com.example.tripster.util.Validator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
        email.setFocusable(false);
        email.setFocusableInTouchMode(false);

        alterFieldEditable(false);
        getUser(SharedPreferencesManager.getUserInfo(getContext()).getUserID());

        hideChangePass();

        binding.edit.setOnCheckedChangeListener((buttonView, isChecked) -> alterFieldEditable(isChecked));

        binding.changePassword.setOnClickListener(v -> {
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
        });

        binding.update.setOnClickListener(v -> {
            if (isValid()) {
                loadUserFromInputs();
                updateUser();
                Toast.makeText(getContext(), "Successfully updated profile.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.delete.setOnClickListener(v -> {
            // TODO: Add dialog for making sure to delete
            deleteUser();
            if (getContext() instanceof MainActivity) {
                MainActivity activity = (MainActivity) getContext();
                activity.logOut();
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

    private void populateWithUserData() {
        name.setText(user.getName());
        surname.setText(user.getSurname());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        country.setText(user.getCountry());
        city.setText(user.getCity());
        postalCode.setText(user.getZipCode());
        streetAndNumber.setText(user.getStreet());
//        binding.streetAndNumber.setText(user.getNumber());
    }

    private void loadUserFromInputs() {
        user.setUserID(SharedPreferencesManager.getUserInfo(getContext()).getUserID());
        user.setId(SharedPreferencesManager.getUserInfo(getContext()).getId());
        user.setName(name.getText().toString().trim());
        user.setSurname(surname.getText().toString().trim());
        user.setEmail(email.getText().toString().trim());
        user.setPhone(phone.getText().toString().trim());
        user.setCountry(country.getText().toString().trim());
        user.setCity(city.getText().toString().trim());
        user.setZipCode(postalCode.getText().toString().trim());
        user.setStreet(streetAndNumber.getText().toString().trim());
//        user.setZipCode(postalCode.getText().toString());
    }


    private boolean isValid() {
        if (validateFormNotEmpty()) {
            Toast.makeText(getContext(), "All fields must be filled!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Validator.isValidEmail(user.getEmail())){
            Toast.makeText(getContext(), "Email format is wrong.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!Validator.isValidPhone(user.getPhone())){
            Toast.makeText(getContext(), "Phone format is wrong." +
                    "", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateFormNotEmpty() {
        return  name.getText().toString().isEmpty() ||
                surname.getText().toString().isEmpty() ||
                email.getText().toString().isEmpty() ||
                phone.getText().toString().isEmpty() ||
                country.getText().toString().isEmpty() ||
                city.getText().toString().isEmpty() ||
                postalCode.getText().toString().isEmpty() ||
                streetAndNumber.getText().toString().isEmpty();
//                number.getText().toString().isEmpty();
    }


    private void alterFieldEditable(boolean editable) {
        name.setFocusable(editable);
        name.setFocusableInTouchMode(editable);
        surname.setFocusable(editable);
        surname.setFocusableInTouchMode(editable);
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

    private void getUser(long id){
        Call<User> call = ClientUtils.userService.getUser(id);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200) {
                    user = response.body();
                    populateWithUserData();
                    Log.d("GET Request", "User " + user);
                } else {
                    Log.e("GET Request", "Error fetching user " + response.code());
                }
                return null;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void updateUser(){
        Call<User> call = ClientUtils.userService.updateUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 201) {
                    Log.d("PUT Request", "User " + user);
                } else {
                    Log.e("PUT Request", "Error updating user " + response.code());
                }
                return null;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void deleteUser(){
        Call<User> call = ClientUtils.userService.deleteUser(SharedPreferencesManager.getUserInfo(getContext()).getUserID());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("DELETE Request", "User " + user);
                if(response.code() == 200) {
                    Log.d("DELETE Request", "User " + user);
                    // TODO: Try to find out why it never reaches any of these logs
//                    if (getContext() instanceof MainActivity) {
//                        MainActivity activity = (MainActivity) getContext();
//                        activity.logOut();
//                    }
                } else {
                    Toast.makeText(getContext(), "You have active reservations.", Toast.LENGTH_SHORT).show();
                    Log.e("DELETE Request", "Error deleting user " + response.code());
                }
                return null;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}