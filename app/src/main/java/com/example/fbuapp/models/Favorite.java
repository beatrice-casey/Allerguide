package com.example.fbuapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * This class handles getting and setting 'favorite' objects in the parse server.
 */

@ParseClassName("Favorites")
public class Favorite extends FavoriteRestaurant {

    public static final String KEY_USER = "User";
    public static final String KEY_RESTAURANT = "Restaurant";
    public static final String KEY_RESTAURANT_NAME = "restaurantName";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseObject getRestaurant() {return getParseObject(KEY_RESTAURANT); }

    public void setRestaurant(ParseObject restaurant) { put(KEY_RESTAURANT, restaurant);}

    public void setRestaurantName(String restaurantName) {put(KEY_RESTAURANT_NAME, restaurantName);}




}
