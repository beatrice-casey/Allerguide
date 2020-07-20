package com.example.fbuapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * This class handles getting and setting the allergies from the parse server for a particular user
 */

@ParseClassName("Allergies")
public class Allergies extends ParseObject {

    public static final String KEY_VEGAN = "Vegan";
    public static final String KEY_VEGETARIAN = "Vegetarian";
    public static final String KEY_GLUTEN_FREE = "glutenFree";
    public static final String KEY_LACTOSE_FREE = "lactoseFree";
    public static final String KEY_USER = "user";

    public boolean getVegan() {
        return getBoolean(KEY_VEGAN);
    }

    public void setVegan(boolean vegan) {
        put(KEY_VEGAN, vegan);
    }

    public boolean getVegetarian() {
        return getBoolean(KEY_VEGETARIAN);
    }

    public void setVegetarian(boolean vegetarian) {
        put(KEY_VEGETARIAN, vegetarian);
    }

    public boolean getGlutenFree() {
        return getBoolean(KEY_GLUTEN_FREE);
    }

    public void setGlutenFree(boolean glutenFree) {
        put(KEY_GLUTEN_FREE, glutenFree);
    }

    public boolean getLactoseFree() {
        return getBoolean(KEY_LACTOSE_FREE);
    }

    public void setLactoseFree(boolean lactoseFree) {
        put(KEY_LACTOSE_FREE, lactoseFree);
    }


    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
}
