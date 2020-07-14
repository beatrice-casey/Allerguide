package com.example.fbuapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    String restaurantName;
    String location;
    String image;

    public Restaurant() { }

    public Restaurant(JSONObject jsonObject) throws JSONException {
        restaurantName = jsonObject.getString("name");
//        location = jsonObject.getString("location");
        image = jsonObject.getString("icon");

//        if (jsonObject.has("geometry")) {
//            JSONObject geometryArray = jsonObject.getJSONObject("geometry");
//            if (geometryArray.has("location")) {
//                JSONArray locationArray = geometryArray.getJSONArray("location");
//                JSONObject latitude = locationArray.getJSONObject(0);
//                JSONObject longitude = locationArray.getJSONObject(1);
//                location = latitude.getString("lat") + longitude.getString("lng");
//            }
//
//        }

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

  //  public String getLocation() { return location;}


}
