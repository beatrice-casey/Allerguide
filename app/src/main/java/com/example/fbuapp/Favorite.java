package com.example.fbuapp;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * This class handles getting and setting 'favorite' objects in the parse server.
 */

@ParseClassName("Favorites")
public class Favorite extends Restaurant{

    public static final String KEY_USER = "User";
    public static final String KEY_RESTAURANT = "Restaurant";
    public static final String KEY_CREATED = "createdAt";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseObject getRestaurant() {return getParseObject(KEY_RESTAURANT); }

    public void setRestaurant(ParseObject restaurant) { put(KEY_RESTAURANT, restaurant);}



}
