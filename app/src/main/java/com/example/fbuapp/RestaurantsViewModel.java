package com.example.fbuapp;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbuapp.fragments.RestaurantsFragment;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class RestaurantsViewModel extends AndroidViewModel {

    public MutableLiveData<List<Restaurant>> restaurants;
    public List<Restaurant> listRestaurants;
    public static final String TAG = "RestaurantsViewModel";
    public String LOCATION_URL;
    public String MAPS_API_KEY;
    public String latitude;
    public String longitude;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */



    public LiveData<List<Restaurant>> getRestaurants() {
        startLocationUpdates();
        restaurants = new MutableLiveData<>();
        listRestaurants = new ArrayList<>();
        queryAllergies();

        return restaurants;
    }

    public RestaurantsViewModel(@NonNull Application application) {
        super(application);
        MAPS_API_KEY = getApplication().getString(R.string.google_maps_API_key);
    }

    private void queryAllergies() {

        //Specify which class to query
        ParseQuery<Allergies> query = ParseQuery.getQuery(Allergies.class);
        //get the user who has the allergies
        query.include(Allergies.KEY_USER);
        query.whereEqualTo(Allergies.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Allergies>() {
            @Override
            public void done(List<Allergies> allergies, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting allergies", e);
                    return;
                }
                for(Allergies allergy : allergies) {
                    Log.i(TAG, "Vegan: " + allergy.getVegan() + " Vegetarian: " + allergy.getVegetarian()
                            + " GF: " + allergy.getGlutenFree() + " LF: " + allergy.getLactoseFree());
                }
                String restrictions = "";
                if(allergies.get(0).getVegan()) {
                    restrictions += "vegan+";
                }
                if(allergies.get(0).getVegetarian()) {

                    restrictions += "vegetarian+";

                }
                if(allergies.get(0).getGlutenFree()) {

                    restrictions += "gluten+free+";


                }
                if(allergies.get(0).getLactoseFree()) {
                    restrictions += "lactose+free+";


                }
                Log.i(TAG, "restrictions are: " + restrictions);
                LOCATION_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + restrictions + "restaurants&location=" + latitude
                        + "," + longitude + "&radius=1500&key=" + MAPS_API_KEY;

                Log.i(TAG, "URL: " + LOCATION_URL);

                loadRestaurants();
            }

        });
    }

    private void loadRestaurants() {
        AsyncHttpClient client = new AsyncHttpClient();


        client.get(LOCATION_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JsonHttpResponseHandler.JSON json) {
                Log.d(TAG, "onSuccess client request");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    listRestaurants.addAll(Restaurant.fromJSONArray(results));
                    Log.i(TAG, listRestaurants.toString());
                    restaurants.setValue(listRestaurants);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Hit json exception ", throwable);
            }
        });

    }

    // Trigger new location updates at interval
    public void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getApplication().getApplicationContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(getApplication().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            latitude = "40.3173";
            longitude = "-74.6199";
            return;
        }
        getFusedLocationProviderClient(getApplication().getApplicationContext()).requestLocationUpdates(mLocationRequest, new
                        LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                // do work here
                                //TODO: use this location to find restaurants near by
                                Location location = locationResult.getLastLocation();
                                latitude = Double.toString(location.getLatitude());
                                longitude = Double.toString(location.getLongitude());


                            }
                        },
                Looper.myLooper());
    }

}
