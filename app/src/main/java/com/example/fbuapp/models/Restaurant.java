package com.example.fbuapp.models;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates a list of Restaurant objects from the JSON array and also adds the restaurant to the Parse database if it it favorited
 *
 */

@ParseClassName("Restaurant")
public class Restaurant extends ParseObject {

    public String restaurantName;
    public String location;
    public String image;
    public static final String KEY_RESTAURANT = "restaurantName";
    public static final String KEY_USER = "User";

    public Restaurant() { }

    public Restaurant(JSONObject jsonObject) throws JSONException {
        restaurantName = jsonObject.getString("name");
        location = jsonObject.getString("formatted_address");
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

    public String getLocation() { return location;}

    public void setRestaurant(String restaurant) {
        put(KEY_RESTAURANT, restaurant);
    }

    public String getRestaurantFromParse() {return getString(KEY_RESTAURANT); }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }



    public Favorite saveFavorite(ParseUser currentUser, ParseObject restaurant) {
        Log.i("Restuarant: ", "current user is " + ParseUser.getCurrentUser());
        Favorite favorite = new Favorite();
        favorite.setRestaurant(restaurant);
        favorite.setUser(currentUser);
        setUser(currentUser);
        favorite.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("Favorite", "Error saving", e);
                }
            }
        });
        restaurant.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null ) {
                    Log.e("Favorite", "Error saving", e);
                }
            }
        });
        Log.i("Restuarant: ", "favorite object is " + favorite);

        return favorite;
    }

    public void deleteFavorite(Favorite favorite, ParseObject restaurant) {
        Log.i("Restuarant: ", "current user is " + ParseUser.getCurrentUser());
        Log.i("Restuarant: ", "favorite object is " + favorite);
        ParseUser currentUser = User.getCurrentUser();
        favorite.setUser(currentUser);
        setUser(currentUser);
        favorite.setRestaurant(restaurant);
        favorite.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("Favorite", "Error saving delete", e);
                }
            }
        });
        restaurant.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("Restaurant", "Error saving delete", e);
                }
            }
        });
    }


}
