package com.example.fbuapp.models;

import android.util.Log;

import com.example.fbuapp.R;
import com.parse.DeleteCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates a list of Restaurant objects from the JSON array and also adds the restaurant to the Parse database if it it favorited
 *
 */
@Parcel
public class Restaurant {

    public String restaurantName;
    public String location;
    public String image;
    public String restaurantID;
    public String photoID;
    public String detailsPhoto;

    public Restaurant() { }

    public Restaurant(JSONObject jsonObject) throws JSONException {
        restaurantName = jsonObject.getString("name");
        location = jsonObject.getString("formatted_address");
        image = jsonObject.getString("icon");
        restaurantID = jsonObject.getString("place_id");

        if (jsonObject.has("photos")) {
            JSONArray photosArray = jsonObject.getJSONArray("photos");
            JSONObject photoObject = photosArray.getJSONObject(0);
            photoID = photoObject.getString("photo_reference");
            detailsPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                    photoID + "&key=" + R.string.google_maps_API_key;

        }

    }

    public static List<Restaurant> fromJSONArray(JSONArray restaurantJsonArray) throws JSONException {
        List<Restaurant> restaurants = new ArrayList<>();
        for(int i = 0; i< restaurantJsonArray.length(); i++) {
            restaurants.add(new Restaurant(restaurantJsonArray.getJSONObject(i)));
        }
        return restaurants;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getImage() {
        return image;
    }

    public String getLocation() { return location;}

    public String getRestaurantID() { return restaurantID; }

    public String getPhotoID() { return photoID; }






}
