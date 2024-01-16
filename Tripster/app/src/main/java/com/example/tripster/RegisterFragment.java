package com.example.tripster;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentRegisterBinding;
import com.example.tripster.model.User;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.util.Validator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment{

    private static AuthorizationActivity AUTHORIZATION = new AuthorizationActivity();

    private AuthorizationActivity authorizationActivity;

    private String item;
    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(AuthorizationActivity param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        AUTHORIZATION = param1;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View view =  inflater.inflate(R.layout.fragment_register, container, false);
        FragmentRegisterBinding binding = FragmentRegisterBinding.bind(view);

        Spinner spinner = binding.spinner;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                item = parent.getItemAtPosition(0).toString();
            }
        });

        ArrayList<String> list = new ArrayList<>();
        list.add("Guest");
        list.add("Host");
        ArrayAdapter<String>adapter = new ArrayAdapter<>(AUTHORIZATION, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

        EditText name = binding.name;
        EditText surname = binding.surname;
        EditText email = binding.email;
        EditText password = binding.password;
        EditText repeated_password = binding.repeatedPassword;
        EditText phone = binding.phone;
        EditText country = binding.country;
        EditText city = binding.city;
        EditText postal_code = binding.postalCode;
        EditText street = binding.street;
        EditText streetNumber = binding.streetNumber;


        Button btnSignUp = binding.btnSignup;
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString().trim();
                String nameText = name.getText().toString().trim();
                String surnameText = surname.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                String repPasswordText = repeated_password.getText().toString().trim();
                String phoneText = phone.getText().toString().trim();
                String countryText = country.getText().toString().trim();
                String cityText = city.getText().toString().trim();
                String postal_codeText = postal_code.getText().toString().trim();
                String streetText = street.getText().toString().trim();
                String streetNumberText = streetNumber.getText().toString().trim();
                if(emailText.isEmpty() || nameText.isEmpty() || surnameText.isEmpty() || passwordText.isEmpty()
                || repPasswordText.isEmpty() || phoneText.isEmpty() || countryText.isEmpty() || cityText.isEmpty()
                || postal_codeText.isEmpty() || streetText.isEmpty() || streetNumberText.isEmpty()){
                    Toast.makeText(getContext(), "All fields must be filled!", Toast.LENGTH_SHORT).show();
                }else if(!Validator.isValidEmail(emailText)){
                    Toast.makeText(getContext(), "Email is in wrong format", Toast.LENGTH_SHORT).show();
                }else if(!Validator.isValidPhone(phoneText)){
                    Toast.makeText(getContext(), "Phone is in wrong format", Toast.LENGTH_SHORT).show();
                } else if (!passwordText.equals(repPasswordText)) {
                    Toast.makeText(getContext(), "Password don't match.", Toast.LENGTH_SHORT).show();
                } else{
                    User user = new User(emailText,passwordText, UserType.valueOf(item.toUpperCase()), nameText,
                            surnameText,phoneText,countryText,cityText,
                            postal_codeText,streetText,streetNumberText);
                    postRegister(user);
                }

            }
        });

        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(LoginFragment.newInstance(AUTHORIZATION, "Ovo je fragment 1"), AUTHORIZATION, false, R.id.downView);
            }
        });
        return view;
    }

    private void postRegister(User user){
        Call<User> call = ClientUtils.authService.register(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                if(response.code() == 201){
                    builder.setTitle("You have successfully registered.")
                            .setMessage("Go to the mail to be verified.");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    FragmentTransition.to(LoginFragment.newInstance(AUTHORIZATION, "Ovo je fragment 1"), AUTHORIZATION, false, R.id.downView);
                }else{
                    builder.setMessage("E-mail doesn't exist.");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }


}