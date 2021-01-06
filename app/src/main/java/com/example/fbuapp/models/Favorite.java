package com.example.fbuapp.models;

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
public class Favorite extends ParseObject {

    public static final String KEY_USER = "User";
    public static final String KEY_RESTAURANT_NAME = "restaurantName";
    public static final String KEY_LOCATION = "Location";
    public static final String KEY_IMAGE = "restaurantImage";
    public static final String KEY_RESTAURANT_ID = "restaurantID";
    public static final String KEY_DETAILS_PHOTO = "detailsPhoto";


    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public void setRestaurantName(String restaurantName) {put(KEY_RESTAURANT_NAME, restaurantName);}

    public String getRestaurantName() {return getString(KEY_RESTAURANT_NAME);}

    public void setKeyLocation(String location) {put(KEY_LOCATION, location);}

    public String getKeyLocation() {return getString(KEY_LOCATION);}

    public void setKeyImage(String imageLink) {put(KEY_IMAGE, imageLink);}

    public String getKeyImage() {return getString(KEY_IMAGE);}

    public void setKeyRestaurantId(String restaurantID) {put(KEY_RESTAURANT_ID, restaurantID);}

    public String getKeyRestaurantId() {return getString(KEY_RESTAURANT_ID);}

    public void setKeyPhotoID(String detailsPhoto) {put(KEY_DETAILS_PHOTO, detailsPhoto);}

    public String getKeyPhotoID() {return getString(KEY_DETAILS_PHOTO);}



}
