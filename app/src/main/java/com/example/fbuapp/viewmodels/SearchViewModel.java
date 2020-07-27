package com.example.fbuapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbuapp.R;
import com.example.fbuapp.models.Allergies;
import com.example.fbuapp.models.Restaurant;
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

public class SearchViewModel extends AndroidViewModel {

    public MutableLiveData<List<Restaurant>> restaurants;
    public List<Restaurant> listRestaurants;
    public static final String TAG = "RestaurantsViewModel";
    public String LOCATION_URL;
    public String MAPS_API_KEY = getApplication().getString(R.string.google_maps_API_key);
    private String location;

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Restaurant>> getRestaurantsAtLocation(String query) {
        restaurants = new MutableLiveData<>();
        listRestaurants = new ArrayList<>();
        queryAllergies();
        location = query;

        return restaurants;
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
                LOCATION_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + restrictions + "restaurants+in+" + location
                        + "&key=" + MAPS_API_KEY;
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
                    int i;
                    for(i = 0; i < Restaurant.fromJSONArray(results).size(); i++) {
                        if (listRestaurants.contains(Restaurant.fromJSONArray(results).get(i).getRestaurantName())) {
                            results.remove(i);
                        }
                    }
                    listRestaurants.addAll(Restaurant.fromJSONArray(results));
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
}
