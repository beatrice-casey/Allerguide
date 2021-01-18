package com.example.fbuapp.favorites;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fbuapp.models.Favorite;
import com.example.fbuapp.models.Restaurant;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for getting the data for the Favorites tab. It makes a query to parse
 * to find restaurants that the current user has added to their favorites.
 */

public class FavoritesViewModel extends AndroidViewModel {

    private MutableLiveData<List<Restaurant>> restaurants;
    private List<Restaurant> listRestaurants;
    public static final String TAG = "FavoritesViewModel";

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Restaurant>> getRestaurants() {
        restaurants = new MutableLiveData<>();
        listRestaurants = new ArrayList<>();
        queryFavorites();

        return restaurants;
    }

    private void queryFavorites() {

        //Specify which class to query
        ParseQuery<Favorite> query = ParseQuery.getQuery(Favorite.class);
        //get the user who's favorite it is
       //query.include(Favorite.KEY_USER);
        query.include(Favorite.KEY_RESTAURANT_NAME);
        query.whereEqualTo(Favorite.KEY_USER, ParseUser.getCurrentUser());
        //query.addDescendingOrder(Favorite.KEY_CREATED);
        query.findInBackground(new FindCallback<Favorite>() {
            @Override
            public void done(List<Favorite> favorites, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting favorites", e);
                    return;
                }
                Log.i("FavoritesViewModel", "Size of favorites: " + favorites.size());
                if (favorites.size() != 0) {
                    int i;
                    for (i = 0; i < favorites.size(); i++) {
                        Favorite favoriteRestaurant = favorites.get(i);
                        Restaurant restaurant = new Restaurant();
                        restaurant.restaurantName = favoriteRestaurant.getRestaurantName();
                        restaurant.location = favoriteRestaurant.getKeyLocation();
                        restaurant.photoID = favoriteRestaurant.getKeyPhotoID();
                        restaurant.restaurantID = favoriteRestaurant.getKeyRestaurantId();
                        listRestaurants.add(restaurant);

                    }

                    restaurants.setValue(listRestaurants);
                    Log.i("FavoritesViewModel", "Restaurants in Parse: " + favorites.get(0).getRestaurantName());

                }
                else {
                    restaurants.setValue(null);
                }
            }
        });
    }
}
