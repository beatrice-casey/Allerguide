package com.example.fbuapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;

import com.example.fbuapp.R;
import com.example.fbuapp.fragments.RestaurantsFragment;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MainActivity";




    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.i(TAG, "google maps URL: " + LOCATION_URL);



        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        fragment = new RestaurantsFragment();
                        //menuItem.setIcon(R.drawable.ic_home);
                        break;
                    case R.id.action_favorites:
                        fragment = new RestaurantsFragment();
                        //menuItem.setIcon(R.drawable.ic_create_fill);
                        break;
                    case R.id.action_search:
                        fragment = new RestaurantsFragment();
                        //menuItem.setIcon(R.drawable.ic_profile);
                        break;
                    case R.id.action_profile:
                        fragment = new RestaurantsFragment();
                        //menuItem.setIcon(R.drawable.ic_profile);
                        break;
                    default:
                        fragment = new RestaurantsFragment();
                        //menuItem.setIcon(R.drawable.ic_home);
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);



    }


}