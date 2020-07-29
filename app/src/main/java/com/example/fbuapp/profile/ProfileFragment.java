package com.example.fbuapp.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fbuapp.R;
import com.example.fbuapp.details.ComposeReviewFragment;
import com.example.fbuapp.login.LoginActivity;
import com.example.fbuapp.models.Restaurant;
import com.example.fbuapp.models.Review;
import com.example.fbuapp.models.User;
import com.example.fbuapp.settings.SettingsFragment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileFragment extends Fragment {

    protected RecyclerView rvReviews;
    protected ProfileAdapter adapter;
    protected List<Review> reviews;
    private Button btnSettings;
    private TextView tvUsername;
    private ProfileViewModel mViewModel;
    private Restaurant restaurant;
    private LinearLayoutManager linearLayoutManager;
    private TextView tvEmptyProfile;
    private ImageView ivProfilePhoto;
    private ParseUser user = User.getCurrentUser();
    private ParseFile photoFile;

    public static final String TAG = "ProfileFragment";


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProviders().of(this).get(ProfileViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvEmptyProfile = view.findViewById(R.id.tvEmptyProfile);
        rvReviews = view.findViewById(R.id.rvUserReviews);
        btnSettings = view.findViewById(R.id.btnSettings);
        tvUsername = view.findViewById(R.id.tvUsername);
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);

        reviews = new ArrayList<>();
        adapter = new ProfileAdapter(getContext(), reviews);
        rvReviews.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvReviews.setLayoutManager(linearLayoutManager);
        tvEmptyProfile.setText("You haven't made any reviews yet! Create a review of a restaurant to find it here");

        mViewModel.getReviews(restaurant).observe(getViewLifecycleOwner(), new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviewsResults) {
                // update UI
                if (!reviewsResults.isEmpty()) {
                    tvEmptyProfile.setText("");
                    adapter.setReviews(reviewsResults);
                }


            }
        });

        tvUsername.setText(ParseUser.getCurrentUser().getUsername());
        btnSettings.setBackgroundResource(R.drawable.ic_baseline_settings_24);

        user.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                Log.i(TAG, "This is the current user object: " + object.getString("username") );
                Log.i(TAG, "This is the current photo object: " + object.getParseFile("profilePhoto") );
                photoFile = object.getParseFile("profilePhoto");
                if (photoFile == null) {
                    ivProfilePhoto.setImageResource(R.drawable.ic_baseline_person_24);
                } else {
                    int radius = 15;
                    int margin = 10;
                    Glide.with(getContext()).load(photoFile.getUrl()).transform(new RoundedCornersTransformation(radius, margin))
                            .into(ivProfilePhoto);

                }
            }
        });



        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SettingsFragment();
                replaceFragment(fragment);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}