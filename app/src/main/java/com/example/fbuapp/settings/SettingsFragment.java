package com.example.fbuapp.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fbuapp.R;
import com.example.fbuapp.login.LoginActivity;
import com.example.fbuapp.models.Allergies;
import com.example.fbuapp.models.Review;
import com.example.fbuapp.models.User;
import com.google.android.material.button.MaterialButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.app.Activity.RESULT_OK;
import static com.example.fbuapp.models.Allergies.KEY_GLUTEN_FREE;
import static com.example.fbuapp.models.Allergies.KEY_LACTOSE_FREE;
import static com.example.fbuapp.models.Allergies.KEY_VEGAN;
import static com.example.fbuapp.models.Allergies.KEY_VEGETARIAN;


public class SettingsFragment extends Fragment {

    public static final String TAG = "Settings";
    CheckBox cbVegan;
    CheckBox cbVegetarian;
    CheckBox cbGlutenFree;
    CheckBox cbLactoseFree;
    MaterialButton btnChangeAllergies;
    TextView tvSelectAllergies;
    ParseUser user;
    Button btnLogout;
    String allergiesObjectID;


    public SettingsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        cbVegan = view.findViewById(R.id.cbVegan);
        cbVegetarian = view.findViewById(R.id.cbVegetarian);
        cbGlutenFree = view.findViewById(R.id.cbGlutenFree);
        cbLactoseFree = view.findViewById(R.id.cbLactoseFree);
        btnChangeAllergies = view.findViewById(R.id.btnUpdateAllergies);
        tvSelectAllergies = view.findViewById(R.id.tvSelectAllergies);
        btnLogout = view.findViewById(R.id.btnLogout);

        user = ParseUser.getCurrentUser();

        showSelectedAllergies();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                //compose icon has been selected
                //navigate to the compose activity
                Intent intent = new Intent(getContext(), LoginActivity.class);
                //start activity
                startActivity(intent);
            }
        });

        btnChangeAllergies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Button clicked!");
                updateAllergies();
            }
        });

    }

    private void updateAllergies() {
        ParseQuery<Allergies> query = ParseQuery.getQuery(Allergies.class);
        Log.i(TAG, "User ID is: " + ParseUser.getCurrentUser().getObjectId());
        Log.i(TAG, "Allergies ID is: " + allergiesObjectID);
        query.whereEqualTo(Allergies.KEY_USER, ParseUser.getCurrentUser());
        query.getInBackground(allergiesObjectID, new GetCallback<Allergies>() {
            public void done(Allergies allergies, ParseException e) {
                if (e == null) {
                    Log.i(TAG, "Current allergies are: " + allergies.get(KEY_VEGAN) + ", " +
                            allergies.get(KEY_VEGETARIAN) + ", " + allergies.get(KEY_LACTOSE_FREE) + ", "
                            + allergies.get(KEY_GLUTEN_FREE));
                    if (cbVegan.isChecked()) {
                        allergies.setVegan(true);
                    } else {
                        allergies.setVegan(false);
                    }
                    if (cbVegetarian.isChecked()) {
                        allergies.setVegetarian(true);
                    } else {
                        allergies.setVegetarian(false);
                    }
                    if (cbGlutenFree.isChecked()) {
                        allergies.setGlutenFree(true);
                    } else {
                        allergies.setGlutenFree(false);
                    }
                    if (cbLactoseFree.isChecked()) {
                        allergies.setLactoseFree(true);
                    } else {
                        allergies.setLactoseFree(false);
                    }

                    allergies.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Issue updating allergies", e);
                                return;
                            }
                            Toast.makeText(getContext(), "Preferences updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void showSelectedAllergies() {
        //Specify which class to query
        ParseQuery<Allergies> query = ParseQuery.getQuery(Allergies.class);
        //get the user who has the allergies
        query.include(Allergies.KEY_USER);
        query.whereEqualTo(Allergies.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Allergies>() {
            @Override
            public void done(List<Allergies> allergies, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting allergies", e);
                    return;
                }
                for(Allergies allergy : allergies) {
                    Log.i(TAG, "Vegan: " + allergy.getVegan() + " Vegetarian: " + allergy.getVegetarian()
                            + " GF: " + allergy.getGlutenFree() + " LF: " + allergy.getLactoseFree());
                }
                if(allergies.get(0).getVegan()) {
                    cbVegan.setChecked(true);
                }
                if(allergies.get(0).getVegetarian()) {
                    cbVegetarian.setChecked(true);
                }
                if(allergies.get(0).getGlutenFree()) {
                    cbGlutenFree.setChecked(true);
                }
                if(allergies.get(0).getLactoseFree()) {
                    cbLactoseFree.setChecked(true);
                }
                allergiesObjectID = allergies.get(0).getObjectId();
                Log.i(TAG, "This is the current ID: " + allergiesObjectID);

            }

        });
    }






}