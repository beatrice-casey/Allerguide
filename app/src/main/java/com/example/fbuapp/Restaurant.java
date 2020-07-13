package com.example.fbuapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    String restaurantName;
    //ArrayList<Integer> location;
    String image;

    public Restaurant() { }

    public Restaurant(JSONObject jsonObject) throws JSONException {
        restaurantName = jsonObject.getString("name");
        //location = jsonObject.get("location");
        image = jsonObject.getString("icon");

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


}
