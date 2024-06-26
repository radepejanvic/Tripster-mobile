package com.example.tripster;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentLoginBinding;
import com.example.tripster.model.Settings;
import com.example.tripster.model.Token;
import com.example.tripster.model.User;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.util.SharedPreferencesManager;
import com.example.tripster.util.Validator;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;


import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static AuthorizationActivity AUTHORIZATION = new AuthorizationActivity();
    private static final String ARG_PARAM2 = "param2";

    private FragmentLoginBinding binding;

    private AuthorizationActivity authorizationActivity;
    private String mParam2;
    private EditText emailInput;
    private EditText passwordInput;
    private Button btnLogin;

    private Button btnRegister;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(AuthorizationActivity param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        AUTHORIZATION = param1;
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        binding = FragmentLoginBinding.bind(view);

        emailInput = binding.name;
        passwordInput = binding.surname;
        btnLogin = binding.btnLogin;
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getContext(), "All fields must be fill!", Toast.LENGTH_SHORT).show();
                }else if(!Validator.isValidEmail(email)){
                    Toast.makeText(getContext(), "Email is in wrong format", Toast.LENGTH_SHORT).show();
                }else{

                    User user = new User(emailInput.getText().toString(),passwordInput.getText().toString());
                    postLogin(user);
                }


            }
        });
        btnRegister = binding.btnRegister;
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransition.to(RegisterFragment.newInstance(AUTHORIZATION, "Ovo je fragment 1"), AUTHORIZATION, false, R.id.downView);
            }
        });

        return view;
    }


    public void postLogin(User user){
        Call<Token> call = ClientUtils.authService.login(user);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    Log.d("REZ",response.body().getToken());

                    JWT jwt = null;
                    JWTClaimsSet claimsSet = null;
                    String email = "";
                    String role = "";
                    try {
                        jwt = JWTParser.parse(response.body().getToken());
                        claimsSet = jwt.getJWTClaimsSet();
                        email = claimsSet.getSubject();
                        Object ro = claimsSet.getClaim("role");
                        JSONArray claim =  (JSONArray) ro;
                        JSONObject object = (JSONObject) claim.get(0);
                        role = object.get("authority").toString();
                        Log.d("AUTHORITY", "onResponse: User authority " + role);
                        role = role.substring(5);
                        Log.d("REZ", role);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }



                    SharedPreferencesManager.saveUserInfo(getContext(), email, UserType.valueOf(role), response.body().getUserID(),response.body().getPersonID(),response.body().getToken());

                    getSettings(response.body().getUserID());

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra("Role", role);
                    Toast.makeText(getContext(), "Welcome "+emailInput.getText().toString(), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    AUTHORIZATION.finish();
                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                    openDialog(response);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void getSettings(Long userId) {
        Call<Settings> call = ClientUtils.settingsService.getSettings(userId);

        call.enqueue(new Callback<Settings>() {
            @Override
            public void onResponse(Call<Settings> call, Response<Settings> response) {
                if(response.code() == 200) {
                    SharedPreferencesManager.saveSettings(getContext(), response.body());
                    Log.d("GET Request", "Settings " + response.body());
                } else {
                    Log.e("GET Request", "Error fetching settings " + response.code());
                }

            }

            @Override
            public void onFailure(Call<Settings> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void openDialog(Response<Token> response) {
        switch (response.code()) {
            case 404:
                Toast.makeText(getContext(), "User doesn't exist.", Toast.LENGTH_SHORT).show();
                break;
            case 403:
                Toast.makeText(getContext(), "User doesn't exist.", Toast.LENGTH_SHORT).show();
                break;
            case 401:
                Toast.makeText(getContext(), "User is suspended.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}