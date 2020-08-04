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
    Button btnAddProfilePhoto;
    Button btnUpdateProfilePhoto;
    ImageView ivProfilePhoto;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final int UPLOAD_IMAGE_ACTIVITY_REQUEST_CODE = 50;
    private File photoFile;
    private String photoFileName = "photo.jpg";
    private ParseFile profilePhoto;


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
        btnAddProfilePhoto = view.findViewById(R.id.btnAddProfilePhoto);
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);
        btnUpdateProfilePhoto = view.findViewById(R.id.btnUpdateProfile);

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

        user.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                Log.i(TAG, "This is the current user object: " + object.getString("username") );
                Log.i(TAG, "This is the current photo object: " + object.getParseFile("profilePhoto") );
                profilePhoto = object.getParseFile("profilePhoto");
                if (photoFile == null) {
                    ivProfilePhoto.setImageResource(R.drawable.ic_baseline_person_24);
                } else {
                    int radius = 15;
                    int margin = 10;
                    Glide.with(getContext()).load(profilePhoto.getUrl()).transform(new RoundedCornersTransformation(radius, margin))
                            .into(ivProfilePhoto);

                }
            }
        });

        btnAddProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });



        btnUpdateProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile(photoFile);
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

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    launchCamera();
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, UPLOAD_IMAGE_ACTIVITY_REQUEST_CODE);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.allerguide.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivProfilePhoto.setImageBitmap(takenImage);

            }
            else if (requestCode == UPLOAD_IMAGE_ACTIVITY_REQUEST_CODE) {
                Uri photoUri = data.getData();

                // Load the image located at photoUri into selectedImage
                Bitmap selectedImage = loadFromUri(photoUri);

                Log.w(TAG, "path of image from gallery" + selectedImage+"");
                ivProfilePhoto.setImageBitmap(selectedImage);

            }
            else { // Result was a failure
                Toast.makeText(getContext(), "Error getting picture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
                photoFile = getPhotoFileUri(photoFileName);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
                photoFile = getPhotoFileUri(photoFileName);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void saveProfile(final File photoFile) {
        final ParseFile parseFile = new ParseFile(photoFile);
        final ParseQuery<User> query = ParseQuery.getQuery(User.class);
        Log.i(TAG, "User ID is: " + ParseUser.getCurrentUser().getObjectId());
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<User>() {
                        public void done(User user, ParseException e) {
                            if (e == null) {
                                Log.i(TAG, "The user is: " + user.getUsername());
                                Log.i(TAG, "The photo file is: " + photoFile);
                                if (photoFile != null) {
                                    user.setProfilePhoto(parseFile);
                                }
                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            Log.e(TAG, "Issue updating profile", e);
                                            return;
                                        }
                                        Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });



    }
}