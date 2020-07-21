package com.example.fbuapp.models;


import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

@ParseClassName("Restaurant")
public class FavoriteRestaurant extends ParseObject {

    public static final String TAG = "FavoriteRestaurant";
    public static final String KEY_RESTAURANT = "restaurantName";
    public static final String KEY_USER = "User";
    boolean queryResult;
    FavoriteRestaurant existingRestaurant;



    public void setRestaurant(String restaurant) {
        put(KEY_RESTAURANT, restaurant);
    }

    public String getRestaurantNameFromParse() {return getString(KEY_RESTAURANT); }

    public ParseObject getRestaurantObject() {return getParseObject(KEY_RESTAURANT);}

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }



    public Favorite saveFavorite(ParseUser currentUser, Restaurant restaurant) {
        Log.i("Restuarant: ", "current user is " + ParseUser.getCurrentUser());
        Favorite favorite = new Favorite();
        FavoriteRestaurant favoriteRestaurant;
        if (queryForRestaurant(restaurant.getRestaurantName())) {
            favoriteRestaurant = existingRestaurant;
        } else {
            favoriteRestaurant = saveRestaurant(restaurant, currentUser);
        }
        favorite.setRestaurant(favoriteRestaurant);
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
        FavoriteRestaurant favoriteRestaurant = new FavoriteRestaurant();
        favoriteRestaurant.setRestaurant(restaurant.getRestaurantName());
        favoriteRestaurant.setUser(currentUser);
        favoriteRestaurant.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null ) {
                    Log.e("Restaurant", "Error saving", e);
                }
            }
        });
        return favoriteRestaurant;
    }


    public void deleteFavorite(Favorite favorite, Restaurant restaurant) {
        Log.i("Restuarant: ", "current user is " + ParseUser.getCurrentUser());
        Log.i("Restuarant: ", "favorite object is " + favorite);
        ParseUser currentUser = User.getCurrentUser();
        favorite.setUser(currentUser);
        //existingRestaurant = new FavoriteRestaurant();
        queryForRestaurant(restaurant.getRestaurantName());
        Log.i(TAG, "Restaurant pointer:" + existingRestaurant);
//        setUser(currentUser);
//        setRestaurant(restaurant.getRestaurantName());
//        Log.i(TAG, "Restaurant is: " + getRestaurantNameFromParse());
//        ParseObject curRestaurant = getRestaurantObject();
//        Log.i(TAG, "Restaurant is: " + curRestaurant);
        if(existingRestaurant != null) {
            favorite.setRestaurant(existingRestaurant);
            favorite.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e("Favorite", "Error saving delete", e);
                    }
                }
            });
        }

    }

    private boolean queryForRestaurant(String restaurantName) {
        //Specify which class to query
        ParseQuery<FavoriteRestaurant> query = ParseQuery.getQuery(FavoriteRestaurant.class);
        query.include(Favorite.KEY_RESTAURANT);
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
                    existingRestaurant = favorites.get(0);
                }
            }

        });
        return queryResult;
    }
}
