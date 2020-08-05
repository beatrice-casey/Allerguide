package com.example.fbuapp.tags;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbuapp.R;
import com.example.fbuapp.models.Allergies;
import com.example.fbuapp.models.Restaurant;
import com.example.fbuapp.models.Tags;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import static com.example.fbuapp.models.Allergies.KEY_GLUTEN_FREE;
import static com.example.fbuapp.models.Allergies.KEY_LACTOSE_FREE;
import static com.example.fbuapp.models.Allergies.KEY_VEGAN;
import static com.example.fbuapp.models.Allergies.KEY_VEGETARIAN;

/**
 * This class is responsible for the tags feature. It shows any selected tags for the restaurant and allows the user to update these tags based on their
 * experience. If the restaurant has no tags, it creates a new tag object in the Parse database for this restaurant and saves the data there.
 */

public class TagsFragment extends Fragment {
    Restaurant restaurant;
    public static final String TAG = "TagsFragment";
    private String tagObjectID;

    CheckBox cbVegan;
    CheckBox cbVegetarian;
    CheckBox cbGlutenFree;
    CheckBox cbLactoseFree;
    MaterialButton btnChangeTags;
    TextView tvSelectAllergies;

    public TagsFragment() {
        // Required empty public constructor
    }

    public TagsFragment(Restaurant restaurant) {
        // Required empty public constructor
        this.restaurant = restaurant;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tags, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cbVegan = view.findViewById(R.id.cbVegan);
        cbVegetarian = view.findViewById(R.id.cbVegetarian);
        cbGlutenFree = view.findViewById(R.id.cbGlutenFree);
        cbLactoseFree = view.findViewById(R.id.cbLactoseFree);
        btnChangeTags = view.findViewById(R.id.btnUpdateTags);
        tvSelectAllergies = view.findViewById(R.id.tvTags);
        showSelectedTags();

        btnChangeTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTags();
            }
        });
    }

    private void showSelectedTags() {
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
                        cbVegan.setChecked(true);
                    }
                    if (tags.get(0).getVegetarian()) {
                        cbVegetarian.setChecked(true);
                    }
                    if (tags.get(0).getGlutenFree()) {
                        cbGlutenFree.setChecked(true);
                    }
                    if (tags.get(0).getLactoseFree()) {
                        cbLactoseFree.setChecked(true);
                    }
                    tagObjectID = tags.get(0).getObjectId();
                    Log.i(TAG, "This is the current ID: " + tagObjectID);
                }
                else {
                    saveRestaurantName();
                }

            }

        });
    }

    private void saveRestaurantName() {
        Tags tag = new Tags();
        tag.setRestaurantName(restaurant.getRestaurantName());
        tag.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with saving new restaurant", e);
                    return;
                }
            }
        });
        showSelectedTags();
    }

    private void createTags() {
        ParseQuery<Tags> query = ParseQuery.getQuery(Tags.class);
        query.whereEqualTo(Tags.KEY_RESTAURANT_NAME, restaurant.getRestaurantName());
        query.getInBackground(tagObjectID, new GetCallback<Tags>() {
            public void done(Tags tags, ParseException e) {
                if (e == null) {
                    Log.i(TAG, "Current allergies are: " + tags.get(KEY_VEGAN) + ", " +
                            tags.get(KEY_VEGETARIAN) + ", " + tags.get(KEY_LACTOSE_FREE) + ", "
                            + tags.get(KEY_GLUTEN_FREE));
                    if (cbVegan.isChecked()) {
                        tags.setVegan(true);
                    } else {
                        tags.setVegan(false);
                    }
                    if (cbVegetarian.isChecked()) {
                        tags.setVegetarian(true);
                    } else {
                        tags.setVegetarian(false);
                    }
                    if (cbGlutenFree.isChecked()) {
                        tags.setGlutenFree(true);
                    } else {
                        tags.setGlutenFree(false);
                    }
                    if (cbLactoseFree.isChecked()) {
                        tags.setLactoseFree(true);
                    } else {
                        tags.setLactoseFree(false);
                    }

                    tags.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Issue updating tags", e);
                                return;
                            }
                            Toast.makeText(getContext(), "Tags updated", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStackImmediate();
                        }
                    });
                }
            }
        });

    }
}