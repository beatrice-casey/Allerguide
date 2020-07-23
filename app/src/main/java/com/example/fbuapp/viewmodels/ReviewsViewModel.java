package com.example.fbuapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.fbuapp.models.Favorite;
import com.example.fbuapp.models.FavoriteRestaurant;
import com.example.fbuapp.models.Restaurant;
import com.example.fbuapp.models.Review;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for getting the data from parse regarding reviews. It queries parse
 * to find the list of reviews for a specific restaurant and passes this information back to the
 * view, where this information can populate the view elements.
 */

public class ReviewsViewModel extends AndroidViewModel {

    public MutableLiveData<List<Review>> reviews;
    public List<Review> listReviews;
    public static final String TAG = "ReviewsViewModel";
    public Restaurant restaurant;

    public ReviewsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Review>> getReviews(Restaurant restaurant) {
        reviews = new MutableLiveData<>();
        listReviews = new ArrayList<>();
        this.restaurant = restaurant;
        Log.i(TAG, "Getting reviews");
        queryReviews();

        return reviews;
    }

    private void queryReviews() {

        //Specify which class to query
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        //get the user who's review it is
        query.include(Review.KEY_USER);
        query.include(Review.KEY_RESTAURANT);
        query.include(Review.KEY_RESTAURANT_NAME);
        query.whereEqualTo(Review.KEY_RESTAURANT_NAME, restaurant.getRestaurantName());
        query.addDescendingOrder(Review.KEY_CREATED);
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> parseReviews, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                if (parseReviews.size() != 0) {
                    listReviews.addAll(parseReviews);
                    for (Review review: parseReviews) {
                        Log.i(TAG, review.getUser().getUsername() + ": " + review.getDescription());
                    }
                    reviews.setValue(listReviews);
                    //Log.i("FavoritesViewModel", "Restaurants in Parse: " + favorites.get(0).getRestaurant());

                }
            }
        });
    }
}
