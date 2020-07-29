package com.example.fbuapp;

import android.app.Application;

import com.example.fbuapp.models.Allergies;
import com.example.fbuapp.models.Favorite;
import com.example.fbuapp.models.FavoriteRestaurant;
import com.example.fbuapp.models.Restaurant;
import com.example.fbuapp.models.Review;
import com.example.fbuapp.models.User;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * This class registers and initialises the parse model.
 */

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        //Register parse model
        ParseObject.registerSubclass(Allergies.class);
        ParseObject.registerSubclass(Favorite.class);
        ParseObject.registerSubclass(FavoriteRestaurant.class);
        ParseUser.registerSubclass(User.class);
        ParseObject.registerSubclass(Review.class);
        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("fbuAppId") // should correspond to APP_ID env variable
                .clientKey("myMasterKeyforfbu")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://fbuapp.herokuapp.com/parse/").build());
    }
}
