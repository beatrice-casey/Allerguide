package com.example.fbuapp.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.fbuapp.R;
import com.example.fbuapp.models.Restaurant;

import org.parceler.Parcels;

/**
 * This class sets up the details activity. It shows the RestaurantDetailsFragment.
 */

public class RestaurantDetailsActivity extends AppCompatActivity {

    public static final String TAG = "RestaurantDetails";
    Context context;
    Restaurant restaurant;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        final FragmentManager fragmentManager = getSupportFragmentManager();


        restaurant = Parcels.unwrap(getIntent().getParcelableExtra(Restaurant.class.getSimpleName()));
        context = this;
        Log.i(TAG, "Restaurant: " + restaurant.getRestaurantName());

        Fragment fragment = new RestaurantDetailsFragment(context, restaurant);

        fragmentManager.beginTransaction().replace(R.id.flContainerReview, fragment).commit();


    }

}