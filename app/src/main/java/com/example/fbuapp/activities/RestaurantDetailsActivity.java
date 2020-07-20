package com.example.fbuapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fbuapp.R;
import com.example.fbuapp.models.Restaurant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.parceler.Parcels;

public class RestaurantDetailsActivity extends AppCompatActivity {

    public static final String TAG = "RestaurantDetails";
    Context context;
    Restaurant restaurant;

    private TextView tvRestaurant;
    private TextView tvLocation;
    private Button btnFavorites;
    private ImageView ivRestaurantImages;
    private TextView tvMenu;
    private TextView tvReviews;
    private FloatingActionButton btnCreateReview;
    private RecyclerView rvReviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);


        tvRestaurant = findViewById(R.id.tvRestaurant);
        tvLocation = findViewById(R.id.tvLocation);
        btnFavorites = findViewById(R.id.btnFavorites);
        ivRestaurantImages = findViewById(R.id.ivRestaurantImages);
        tvMenu = findViewById(R.id.tvMenu);
        tvReviews = findViewById(R.id.tvReviewTag);
        btnCreateReview = findViewById(R.id.btnAddReview);
        rvReviews = findViewById(R.id.rvReviews);


        restaurant = Parcels.unwrap(getIntent().getParcelableExtra(Restaurant.class.getSimpleName()));
        context = this;
        Log.i(TAG, "Restaurant: " + restaurant.getRestaurantName());

        tvRestaurant.setText(restaurant.getRestaurantName());
        tvLocation.setText(restaurant.getLocation());

    }
}