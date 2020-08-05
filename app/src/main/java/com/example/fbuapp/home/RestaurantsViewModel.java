package com.example.fbuapp.home;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbuapp.R;
import com.example.fbuapp.models.Allergies;
import com.example.fbuapp.models.Restaurant;
import com.example.fbuapp.models.Tags;
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

/**
 * This class is the View Model that handles getting the data that will be displayed. It makes the API call and queries to the parse
 * server to get the user's allergies in order to make the appropriate API call. Once it receives the list of restaurants, it then sorts it
 * to show the restaurants that have tags that match the user's specific allergies at the top of the list.
 * It also handles location updates and will update the API call according to location.
 */

public class RestaurantsViewModel extends AndroidViewModel {

    private MutableLiveData<List<Restaurant>> restaurants;
    private List<Restaurant> listRestaurants;
    private List<Restaurant> sortedRestaurants;
    public List<Restaurant> noMatchRestaurants;
    public static final String TAG = "RestaurantsViewModel";
    private String LOCATION_URL;
    private String MAPS_API_KEY;
    private String latitude;
    private String longitude;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private static final int COARSE_LOCATION_PERMISSION_CODE = 100;
    private static final int FINE_LOCATION_PERMISSION_CODE = 101;
    private AsyncHttpClient client = new AsyncHttpClient();
    private String tagsString;
    private String restrictions = "";


    public LiveData<List<Restaurant>> getRestaurants() {
        startLocationUpdates();
        restaurants = new MutableLiveData<>();
        listRestaurants = new ArrayList<>();
        queryAllergies();

        return restaurants;
    }
    public LiveData<List<Restaurant>> loadMoreRestaurants() {
        restaurants = new MutableLiveData<>();
        listRestaurants = new ArrayList<>();
        loadNextPageRestaurants();

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

        client.get(LOCATION_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JsonHttpResponseHandler.JSON json) {
                Log.d(TAG, "onSuccess client request");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    int i;
                    for(i = 0; i < Restaurant.fromJSONArray(results).size(); i++) {
                        if (listRestaurants.contains(Restaurant.fromJSONArray(results).get(i).getRestaurantName())) {
                            results.remove(i);
                        }
                    }
                    listRestaurants.addAll(Restaurant.fromJSONArray(results));
                    List<Restaurant> finalSort;
                    finalSort = sortByTags(listRestaurants);
                    restaurants.setValue(finalSort);

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

            ActivityCompat.requestPermissions((Activity) getApplication().getApplicationContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    FINE_LOCATION_PERMISSION_CODE);
            return;
        }
        getFusedLocationProviderClient(getApplication().getApplicationContext()).requestLocationUpdates(mLocationRequest, new
                        LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                // do work here
                                Location location = locationResult.getLastLocation();
                                latitude = Double.toString(location.getLatitude());
                                longitude = Double.toString(location.getLongitude());


                            }
                        },
                Looper.myLooper());
    }

    private void loadNextPageRestaurants() {
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);
        params.put("truncated", false);
        params.put("include_entities", true);
        Log.i(TAG, "loading more data with this URL: " + LOCATION_URL);
        client.get(LOCATION_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess for loadMoreData " + json.toString());
                //2. Deserialize and construct new model objects from the API response
                JSONArray jsonArray = json.jsonArray;
                try {
                    if (jsonArray != null) {
                        List<Restaurant> newRestaurants = Restaurant.fromJSONArray(jsonArray);
                        //3. Append the new data objects to the existing set of items inside the array of items
                        //4. Notify the adapter of new items made with notifyItemsRangeInserted()
                        listRestaurants.addAll(newRestaurants);
                        restaurants.setValue(listRestaurants);
                    }

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

    private List<Restaurant> sortByTags(final List<Restaurant> restaurantsToSort) {

        sortedRestaurants = new ArrayList<>();

        int i;
        for (i = 0; i < listRestaurants.size(); i++) {
            queryTags(restaurantsToSort.get(i));
        }
        return sortedRestaurants;

    }


    private void queryTags(final Restaurant queryRestaurant) {
        //Specify which class to query
        ParseQuery<Tags> query = ParseQuery.getQuery(Tags.class);
        query.include(Tags.KEY_RESTAURANT_NAME);
        query.whereEqualTo(Tags.KEY_RESTAURANT_NAME, queryRestaurant.getRestaurantName());
        query.findInBackground(new FindCallback<Tags>() {
            @Override
            public void done(List<Tags> tags, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting allergies", e);
                    return;
                }
                for (Tags tag : tags) {
                    Log.i(TAG, "Vegan: " + tag.getVegan() + " Vegetarian: " + tag.getVegetarian()
                            + " GF: " + tag.getGlutenFree() + " LF: " + tag.getLactoseFree());
                }
                Log.i(TAG, "Restrictions are: " +restrictions);
                if (!tags.isEmpty()) {

                    if (tags.get(0).getVegan()) {
                        tagsString += "vegan+";
                    }
                    if (tags.get(0).getVegetarian()) {
                        tagsString += "vegetarian+";
                    }
                    if (tags.get(0).getGlutenFree()) {
                        tagsString += "gluten+free+";
                    }
                    if (tags.get(0).getLactoseFree()) {
                        tagsString += "lactose+free+";
                    }
                    Log.i(TAG, "Tags are: " + tagsString);
                    if (tagsString.contains(restrictions)) {
                        Log.i(TAG, "Tags contained restrictions for : " + queryRestaurant.getRestaurantName());
                        sortedRestaurants.add(0, queryRestaurant);
                        listRestaurants.remove(queryRestaurant);
                    }
                    tagsString = "";

                }
                if (!sortedRestaurants.contains(queryRestaurant)) {
                    Log.i(TAG, "This restaurant was not added: " + queryRestaurant.getRestaurantName());
                    sortedRestaurants.add(queryRestaurant);
                }
            }

        });



    }

}

