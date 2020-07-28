package com.example.fbuapp.search;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
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
    private AsyncHttpClient client = new AsyncHttpClient();

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

    public LiveData<List<Restaurant>> loadMoreRestaurants() {
        restaurants = new MutableLiveData<>();
        listRestaurants = new ArrayList<>();
        loadNextPageRestaurants();

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

}
