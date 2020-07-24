package com.example.fbuapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fbuapp.R;
import com.example.fbuapp.activities.LoginActivity;
import com.example.fbuapp.adapters.ProfileAdapter;
import com.example.fbuapp.adapters.ReviewsAdapter;
import com.example.fbuapp.models.Restaurant;
import com.example.fbuapp.models.Review;
import com.example.fbuapp.models.User;
import com.example.fbuapp.viewmodels.ProfileViewModel;
import com.example.fbuapp.viewmodels.ReviewsViewModel;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    protected RecyclerView rvReviews;
    protected ProfileAdapter adapter;
    protected List<Review> reviews;
    private Button btnLogout;
    private TextView tvUsername;
    private ProfileViewModel mViewModel;
    private Restaurant restaurant;
    private LinearLayoutManager linearLayoutManager;


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
        rvReviews = view.findViewById(R.id.rvUserReviews);
        btnLogout = view.findViewById(R.id.btnLogout);
        tvUsername = view.findViewById(R.id.tvUsername);

        reviews = new ArrayList<>();
        adapter = new ProfileAdapter(getContext(), reviews);
        rvReviews.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvReviews.setLayoutManager(linearLayoutManager);

        mViewModel.getReviews(restaurant).observe(getViewLifecycleOwner(), new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviewsResults) {
                // update UI
                adapter.setReviews(reviewsResults);


            }
        });

        tvUsername.setText(ParseUser.getCurrentUser().getUsername());
        btnLogout.setBackgroundResource(R.drawable.ic_baseline_settings_24);

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
    }
}