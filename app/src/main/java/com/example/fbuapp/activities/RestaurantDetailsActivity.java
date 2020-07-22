package com.example.fbuapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbuapp.R;
import com.example.fbuapp.fragments.ComposeReviewFragment;
import com.example.fbuapp.fragments.RestaurantDetailsFragment;
import com.example.fbuapp.models.Restaurant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

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