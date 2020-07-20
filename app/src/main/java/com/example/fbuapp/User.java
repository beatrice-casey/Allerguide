package com.example.fbuapp;


import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseUser {

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ALLERGIES = "allergies";
    public static final String KEY_FAVORITES = "favorites";

    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    public void setUsername(String username) {
        put(KEY_USERNAME, username);
    }

    public ParseFile getPassword() {
        return getParseFile(KEY_PASSWORD);
    }

    public void setPassword(ParseFile password) {
        put(KEY_PASSWORD, password);
    }

    public ParseUser getUser() {
        return getCurrentUser();
    }

    public ParseObject getAllergies() {return getParseObject(KEY_ALLERGIES);}

    public ParseObject getFavorites() {return getParseObject(KEY_FAVORITES);}
}
