package com.sebrs3018.SmartSharing;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;
import com.sebrs3018.SmartSharing.databinding.ActivityNavigationBinding;
import com.sebrs3018.SmartSharing.ui.dashboard.DashboardFragment;
import com.sebrs3018.SmartSharing.ui.home.HomeFragment;
import com.sebrs3018.SmartSharing.ui.search.SearchFragment;

import org.jetbrains.annotations.NotNull;

public class Navigation_Activity extends AppCompatActivity {

    private ActivityNavigationBinding binding;
//    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private final String TAG = "Navigation_Activity";
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

/*        bottomNavigationView = findViewById(R.id.bn_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);*/

        /* Setting up the bottomNavigationView */
        binding.bnView.setOnNavigationItemSelectedListener(navListener);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_search)
                                                     .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_navigation);
        NavController navController = navHostFragment.getNavController();
        /* Setto NavigationUI con la bottomNavBar */
        NavigationUI.setupWithNavController(binding.bnView, navController);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater mMenu = getMenuInflater();
        mMenu.inflate(R.menu.appbar_menu, menu);

        return true;
    }

/*    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_navigation);
        NavController navController = navHostFragment.getNavController();
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/


    /* Dichiarazione metodo privato listener */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = new DashboardFragment();
                    break;
            }

            /* Assegno fragment al mio root */
            if(selectedFragment != null)
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_navigation, selectedFragment).commit();

            return true;
        }
    };

    //TODO: da valutare se rimuoverlo o meno
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}