package com.example.tripster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AuthorizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        FragmentTransition.to(LoginFragment.newInstance(AuthorizationActivity.this, "Ovo je fragment 1"), AuthorizationActivity.this, false, R.id.downView);

    }
}