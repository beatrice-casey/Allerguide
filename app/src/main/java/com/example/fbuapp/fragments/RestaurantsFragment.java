package com.example.fbuapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbuapp.R;
import com.example.fbuapp.models.Restaurant;
import com.example.fbuapp.adapters.RestaurantsAdapter;
import com.example.fbuapp.viewmodels.RestaurantsViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

/**
 * This class is the fragment that shows the user the list of restaurants near them. This class handles ONLY the view.
 */

public class RestaurantsFragment extends Fragment {

    public static final String TAG = "RestaurantsFragment";
    protected RecyclerView rvRestaurants;
    protected List<Restaurant> restaurants;
    protected RestaurantsAdapter adapter;

    private RestaurantsViewModel mViewModel;
    LinearLayoutManager linearLayoutManager;


    public RestaurantsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProviders().of(this).get(RestaurantsViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurants, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvRestaurants = view.findViewById(R.id.rvRestaurants);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvRestaurants.setLayoutManager(linearLayoutManager);

        restaurants = new ArrayList<>();
        adapter = new RestaurantsAdapter(getContext(), restaurants);
        rvRestaurants.setAdapter(adapter);


        mViewModel.getRestaurants().observe(getViewLifecycleOwner(), new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {
                // update UI
                adapter.setRestaurants(restaurants);
            }
        });


    }
}
