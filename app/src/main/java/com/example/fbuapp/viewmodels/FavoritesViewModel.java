package com.example.fbuapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fbuapp.models.FavoriteRestaurant;
import com.example.fbuapp.models.Restaurant;
import com.example.fbuapp.models.Favorite;
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

    public MutableLiveData<List<FavoriteRestaurant>> restaurants;
    public List<FavoriteRestaurant> listRestaurants;
    public static final String TAG = "FavoritesViewModel";

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<FavoriteRestaurant>> getRestaurants() {
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
        query.include(Favorite.KEY_RESTAURANT);
        query.whereEqualTo(Favorite.KEY_USER, ParseUser.getCurrentUser());
        //query.addDescendingOrder(Favorite.KEY_CREATED);
        query.findInBackground(new FindCallback<Favorite>() {
            @Override
            public void done(List<Favorite> favorites, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                if (favorites.size() != 0) {
                    int i;
                    for (i = 0; i < favorites.size(); i++) {
                        FavoriteRestaurant favoriteRestaurant = (FavoriteRestaurant) favorites.get(i).getRestaurant();
                        listRestaurants.add(favoriteRestaurant);
                    }
                    restaurants.setValue(listRestaurants);
                    Log.i("FavoritesViewModel", "Restaurants in Parse: " + favorites.get(0).getRestaurant());

                }
            }
        });
    }
}
