package com.example.fbuapp.profile;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fbuapp.details.ReviewsViewModel;
import com.example.fbuapp.models.Review;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileViewModel extends ReviewsViewModel {


    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }



    @Override
    protected void queryReviews() {

        //Specify which class to query
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        //get the user who's review it is
        query.include(Review.KEY_USER);
        query.include(Review.KEY_RESTAURANT);
        query.include(Review.KEY_RESTAURANT_NAME);
        query.whereEqualTo(Review.KEY_USER, ParseUser.getCurrentUser());
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
