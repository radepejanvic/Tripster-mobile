package com.example.tripster;

import android.os.Bundle;

import com.example.tripster.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private BottomNavigationView navigationBar;
    private UserType userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        navigationBar = binding.navBar;

        userType = UserType.randomUserType();

//        navigationBar.getMenu().findItem(R.id.navigation_reservations).setVisible(false);

        navigationBar.inflateMenu(getRoleMenu());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_reservations, R.id.navigation_notifications, R.id.navigation_account, R.id.navigation_blank)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
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


}