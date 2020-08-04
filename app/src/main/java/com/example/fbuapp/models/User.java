package com.example.fbuapp.models;


import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * This class handles getting and setting values for users from the User model in the parse database
 */

@ParseClassName("_User")
public class User extends ParseUser {

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ALLERGIES = "allergies";
    public static final String KEY_FAVORITES = "favorites";
    public static final String KEY_PROFILE_PHOTO = "profilePhoto";

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

    public ParseFile getProfilePhoto() {
        return getParseFile(KEY_PROFILE_PHOTO);
    }

    public void setProfilePhoto(ParseFile parseFile) {
        put(KEY_PROFILE_PHOTO, parseFile);
    }

}
