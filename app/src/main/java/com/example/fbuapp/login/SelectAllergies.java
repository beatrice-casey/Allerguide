package com.example.fbuapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.fbuapp.home.MainActivity;
import com.example.fbuapp.models.Allergies;
import com.example.fbuapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * This class handles the selection of allergies. It uses a material design card to allow the user
 * to select the appropriate allergies. This data is saved via the parse server so that the
 * user's allergies are remembered anytime they log into the app. Once the user selects their
 * allergies, they are allowed to use the app and can view the list of restaurants that have
 * food that are allergy-freindly according to what they specified.
 */

public class SelectAllergies extends AppCompatActivity {

    private static final String TAG = "SelectAllergies";
    private MaterialCardView cardView;
    private CheckBox cbVegan;
    private CheckBox cbVegetarian;
    private CheckBox cbGlutenFree;
    private CheckBox cbLactoseFree;
    private MaterialButton btnSetAllergies;
    private TextView tvSelectAllergies;
    private TextView tvChangePreferences;
   private ParseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_allergies);

        cardView = findViewById(R.id.selectAllergiesCard);
        cbVegan = findViewById(R.id.cbVegan);
        cbVegetarian = findViewById(R.id.cbVegetarian);
        cbGlutenFree = findViewById(R.id.cbGlutenFree);
        cbLactoseFree = findViewById(R.id.cbLactoseFree);
        btnSetAllergies = findViewById(R.id.btnSetAllergies);
        tvSelectAllergies = findViewById(R.id.tvSelectAllergies);
        tvChangePreferences = findViewById(R.id.tvChangePreferences);

        user = ParseUser.getCurrentUser();



        btnSetAllergies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreferences();
            }
        });
    }

    private void savePreferences() {
        Allergies allergies = new Allergies();
        if (cbVegan.isChecked()) {
            allergies.setVegan(true);

        }
        if (cbVegetarian.isChecked()) {
            allergies.setVegetarian(true);

        }
        if (cbGlutenFree.isChecked()) {
            allergies.setGlutenFree(true);

        }
        if (cbLactoseFree.isChecked()) {
            allergies.setLactoseFree(true);

        }
        allergies.setUser(user);



        allergies.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                }
                goMainActivity();
            }
        });

    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}