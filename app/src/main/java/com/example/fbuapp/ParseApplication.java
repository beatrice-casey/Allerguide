package com.example.fbuapp;

import android.app.Application;

import com.example.fbuapp.models.Allergies;
import com.example.fbuapp.models.Favorite;
import com.example.fbuapp.models.Review;
import com.example.fbuapp.models.Tags;
import com.example.fbuapp.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * This class registers and initialises the parse model.
 */

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Allergies.class);
        ParseObject.registerSubclass(Tags.class);
        ParseObject.registerSubclass(Review.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Favorite.class);

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("aaYp82IxA1rFWPEL3CdsFRoRh7eGMBj6BLvfMF5R") // should correspond to Application Id env variable
                .clientKey("IZQtqUXSMaLVeUyffePEq5Qzzye71CGj2pG9Hdc5")  // should correspond to Client key env variable
                        .server("https://parseapi.back4app.com").build());
    }
}
