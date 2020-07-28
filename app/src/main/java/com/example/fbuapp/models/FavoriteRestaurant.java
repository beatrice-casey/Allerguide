package com.example.fbuapp.models;


import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * This class creates a new object in the Parse database for favorite restaurants. It takes the data from
 * the API and saves it to parse. It also saves a new favorite and deletes a favorite. Finally, it
 * checks the database to see if the restaurant already is there so it doesn't make a duplicate entry.
 */

@ParseClassName("Restaurant")
public class FavoriteRestaurant extends ParseObject {

    public static final String TAG = "FavoriteRestaurant";
    public static final String KEY_RESTAURANT = "restaurantName";
    public static final String KEY_USER = "User";
    public static final String KEY_LOCATION = "Location";
    public static final String KEY_PHOTO = "restaurantImage";
    boolean queryResult;
    FavoriteRestaurant existingRestaurant;



    public void setRestaurant(String restaurant) {
        put(KEY_RESTAURANT, restaurant);
    }

    public String getRestaurantNameFromParse() {return getString(KEY_RESTAURANT); }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getLocation() {return getString(KEY_LOCATION);}

    public void setLocation(String location) { put(KEY_LOCATION, location);}

    public String getImage() {
        return getString(KEY_PHOTO);
    }

    public void setImage(String photoID) {
        put(KEY_PHOTO, photoID);
    }




    public Favorite saveFavorite(ParseUser currentUser, FavoriteRestaurant favoriteRestaurant) {
        Log.i("Restuarant: ", "current user is " + ParseUser.getCurrentUser());
        Favorite favorite = new Favorite();
        favorite.setRestaurant(favoriteRestaurant);
        favorite.setRestaurantName(favoriteRestaurant.getRestaurantNameFromParse());
        favorite.setUser(currentUser);
        favorite.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("Favorite", "Error saving", e);
                }
            }
        });

        Log.i("Restuarant: ", "favorite object is " + favorite);

        return favorite;
    }

    public FavoriteRestaurant saveRestaurant(Restaurant restaurant, ParseUser currentUser) {
        FavoriteRestaurant favoriteRestaurant;
        if (queryForRestaurant(restaurant.getRestaurantName())) {
            favoriteRestaurant = existingRestaurant;
        }
        else {
            favoriteRestaurant = new FavoriteRestaurant();
            favoriteRestaurant.setRestaurant(restaurant.getRestaurantName());
            favoriteRestaurant.setLocation(restaurant.getLocation());
            favoriteRestaurant.setImage(restaurant.getPhotoID());
            favoriteRestaurant.setUser(currentUser);
            favoriteRestaurant.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null ) {
                        Log.e("Restaurant", "Error saving", e);
                    }
                }
            });
        }
        return favoriteRestaurant;
    }


    public void deleteFavorite(Favorite favorite, FavoriteRestaurant restaurant) {
        Log.i("Restuarant: ", "current user is " + ParseUser.getCurrentUser());
        Log.i("Restuarant: ", "favorite object is " + favorite.getRestaurantNameFromParse());
        ParseUser currentUser = User.getCurrentUser();
        favorite.setUser(currentUser);
        favorite.setRestaurant(restaurant);
        favorite.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("Favorite", "Error saving delete", e);
                }
            }
        });

    }

    private boolean queryForRestaurant(final String restaurantName) {
        //Specify which class to query
        ParseQuery<FavoriteRestaurant> query = ParseQuery.getQuery(FavoriteRestaurant.class);
        query.include(FavoriteRestaurant.KEY_RESTAURANT);
        query.whereEqualTo(FavoriteRestaurant.KEY_RESTAURANT, restaurantName);
        //query.addDescendingOrder(Favorite.KEY_CREATED);
        query.findInBackground(new FindCallback<FavoriteRestaurant>() {
            @Override
            public void done(List<FavoriteRestaurant> favorites, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                if (favorites.size() == 0) {
                    queryResult =  false;

                } else {
                    queryResult = true;
                    Log.i(TAG, "query result: " + favorites);
                    int i;
                    for(i = 0; i < favorites.size(); i++) {
                        if (favorites.get(i).getRestaurantNameFromParse().equals(restaurantName)) {
                            existingRestaurant = favorites.get(i);
                            Log.i(TAG, "The restaurant is: " + existingRestaurant.getRestaurantNameFromParse());
                        }
                    }

                }
            }

        });
        return queryResult;
    }
}
