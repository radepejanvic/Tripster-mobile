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
import com.example.tripster.databinding.FragmentAccommodationsBinding;
import com.example.tripster.databinding.FragmentLoginBinding;
import com.example.tripster.model.User;
import com.example.tripster.model.UserType;
import com.example.tripster.util.SharedPreferencesManager;
import com.google.android.material.textfield.TextInputEditText;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;


import org.json.JSONException;


import java.text.ParseException;
import java.util.List;
import java.util.Map;

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

    public LoginFragment() {
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
        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("REZ", emailInput.getText().toString()+" "+passwordInput.getText().toString());
                User user = new User(emailInput.getText().toString(),passwordInput.getText().toString());
                postLogin(user);
            }
        });
        Button btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransition.to(RegisterFragment.newInstance(AUTHORIZATION, "Ovo je fragment 1"), AUTHORIZATION, false, R.id.downView);
            }
        });

        return view;
    }


    public void postLogin(User user){
        Call<User> call = ClientUtils.authService.login(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
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
                        role = role.substring(5);
                        Log.d("REZ", role);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    SharedPreferencesManager.saveUserInfo(AUTHORIZATION, email, UserType.valueOf(role), response.body().getUserID(),response.body().getPersonID());
                    Intent intent = new Intent(AUTHORIZATION, MainActivity.class);
                    intent.putExtra("Role", role);
                    Toast.makeText(getContext(), "Welcome "+emailInput.getText().toString(), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    AUTHORIZATION.finish();
                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                    openDialog();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
//                Toast.makeText(getContext(), "Welcome "+emailInput.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialog() {
    }

    ;

}