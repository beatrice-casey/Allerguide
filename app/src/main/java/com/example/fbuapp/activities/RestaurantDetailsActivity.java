package com.example.fbuapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbuapp.R;
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

    private TextView tvRestaurant;
    private TextView tvLocation;
    private Button btnFavorites;
    private ImageView ivRestaurantImages;
    private TextView tvMenu;
    private TextView tvReviews;
    private FloatingActionButton btnCreateReview;
    private RecyclerView rvReviews;
    private String RESTAURANT_WEBSITE_URL;
    private String restaurantWebsite;


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

        tvMenu.setText(getRestaurantWebsite());
        Log.i(TAG, "Done setting elements");

    }

    private String getRestaurantWebsite() {
        AsyncHttpClient client = new AsyncHttpClient();

        RESTAURANT_WEBSITE_URL = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" +
                restaurant.getRestaurantID() + "&fields=website&key=" + getString(R.string.google_maps_API_key);
        Log.i(TAG, "URL: " + RESTAURANT_WEBSITE_URL);

        client.get(RESTAURANT_WEBSITE_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JsonHttpResponseHandler.JSON json) {
                Log.d(TAG, "onSuccess client request");
                JSONObject jsonObject = json.jsonObject;
                try {
                    restaurantWebsite = jsonObject.getString("result");
                    Log.i(TAG, restaurantWebsite);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Hit json exception ", throwable);
            }
        });
        return restaurantWebsite;
    }
}