package com.example.fbuapp.tags;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fbuapp.models.Restaurant;
import com.example.fbuapp.models.Review;
import com.example.fbuapp.models.Tags;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * This class finds the tags (if any) a restaurant may have.
 */

public class TagsViewModel extends AndroidViewModel {
    public MutableLiveData<String> tagsMutableLiveData;
    private String tagsString;
    public Restaurant restaurant;
    public static final String TAG = "TagsViewModel";

    public TagsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getTags(Restaurant restaurant) {
        tagsMutableLiveData = new MutableLiveData<>();
        tagsString = "";
        this.restaurant = restaurant;

        queryTags();

        return tagsMutableLiveData;
    }

    private void queryTags() {
        //Specify which class to query
        ParseQuery<Tags> query = ParseQuery.getQuery(Tags.class);
        query.include(Tags.KEY_RESTAURANT_NAME);
        query.whereEqualTo(Tags.KEY_RESTAURANT_NAME, restaurant.getRestaurantName());
        query.findInBackground(new FindCallback<Tags>() {
            @Override
            public void done(List<Tags> tags, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting allergies", e);
                    return;
                }
                for (Tags tag : tags) {
                    Log.i(TAG, "Vegan: " + tag.getVegan() + " Vegetarian: " + tag.getVegetarian()
                            + " GF: " + tag.getGlutenFree() + " LF: " + tag.getLactoseFree());
                }
                if (!tags.isEmpty()) {

                    if (tags.get(0).getVegan()) {
                        tagsString += "Vegan ";
                    }
                    if (tags.get(0).getVegetarian()) {
                        tagsString += "Vegetarian ";
                    }
                    if (tags.get(0).getGlutenFree()) {
                        tagsString += "GF ";
                    }
                    if (tags.get(0).getLactoseFree()) {
                        tagsString += "Lactose Free ";
                    }
                    tagsMutableLiveData.setValue(tagsString);

                }

            }

        });
    }


}
