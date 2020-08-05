package com.example.fbuapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * This class handles getting and setting Tag objects in the Parse database.
 */

@ParseClassName("Tags")
public class Tags extends ParseObject {

    public static final String KEY_VEGAN = "vegan";
    public static final String KEY_VEGETARIAN = "vegetarian";
    public static final String KEY_GLUTEN_FREE = "glutenFree";
    public static final String KEY_LACTOSE_FREE = "lactoseFree";
    public static final String KEY_RESTAURANT_NAME = "restaurantName";

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

    public String getRestaurantName() { return getString(KEY_RESTAURANT_NAME);}

    public void setRestaurantName(String restaurantName) { put(KEY_RESTAURANT_NAME, restaurantName); }
}
