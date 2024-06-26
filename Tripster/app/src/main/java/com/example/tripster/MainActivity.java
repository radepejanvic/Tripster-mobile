package com.example.tripster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.tripster.databinding.ActivityMainBinding;
import com.example.tripster.fragment.notifications.NotificationListFragment;
import com.example.tripster.fragment.notifications.NotificationsFragment;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.util.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private BottomNavigationView navigationBar;
    private UserType userType;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        navigationBar = binding.navBar;

        userType = UserType.valueOf(getIntent().getStringExtra("Role"));

        navigationBar.inflateMenu(getRoleMenu());

        navController = Navigation.findNavController(this, R.id.nav_fragment_activity_main);
        NavigationUI.setupWithNavController(navigationBar, navController);
    }

    private int getRoleMenu() {
        switch (userType) {
            case ADMIN:
                return R.menu.bottom_nav_menu_admin;
            case HOST:
                return R.menu.bottom_nav_menu_host;
            default:
                return R.menu.bottom_nav_menu_guest;
        }
    }

    private int getToolbarMenu() {
        switch (userType) {
            case ADMIN:
                return R.menu.top_nav_menu_admin;
            case HOST:
                return R.menu.top_nav_menu_host;
            default:
                return R.menu.top_nav_menu_guest;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(getToolbarMenu(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.navigation_logout) {
            logOut();
        } else if (item.getItemId() == R.id.navigation_notifications) {
            navController.navigate(R.id.navigation_notifications);
        } else if (item.getItemId() == R.id.navigation_settings) {
            navController.navigate(R.id.navigation_settings);
        }
        return super.onOptionsItemSelected(item);
    }



    public void logOut() {
        SharedPreferencesManager.clearUserInfo(this);
        SharedPreferencesManager.clearSettings(this);

        Intent intent = new Intent(this, AuthorizationActivity.class);
        startActivity(intent);
        finish();
    }
}