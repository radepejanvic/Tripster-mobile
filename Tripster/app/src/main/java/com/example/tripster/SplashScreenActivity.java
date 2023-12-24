package com.example.tripster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.example.tripster.model.User;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                User user = SharedPreferencesManager.getUserInfo(SplashScreenActivity.this);
                if (user != null && !TextUtils.isEmpty(user.getEmail())) {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.putExtra("Role", user.getUserType().toString());
                    startActivity(intent);
                    finish();
                    return;
                }
                startActivity(new Intent(SplashScreenActivity.this, AuthorizationActivity.class));
                finish();
            }
        },4000);
    }
}