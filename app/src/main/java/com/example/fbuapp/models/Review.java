package com.example.fbuapp.models;


import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * This class handles getting and setting information from the Review model in the parse database.
 */

@ParseClassName("Review")
public class Review extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "Photo";
    public static final String KEY_USER = "username";
    public static final String KEY_CREATED = "createdAt";
    public static final String KEY_RATING = "Rating";
    public static final String KEY_RESTAURANT = "Restaurant";
    public static final String KEY_RESTAURANT_NAME = "restaurantName";


    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public float getRating() { return getLong(KEY_RATING);}

    public void setRating(float rating) { put(KEY_RATING, rating);}

    public ParseObject getRestaurant() { return getParseObject(KEY_RESTAURANT);}

    public void setRestaurant(ParseObject restaurant) {put(KEY_RESTAURANT, restaurant);}

    public String getRestaurantName() {return getString(KEY_RESTAURANT_NAME);}

    public void setRestaurantName(String restaurantName) {put(KEY_RESTAURANT_NAME, restaurantName);}

}