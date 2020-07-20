package com.example.fbuapp.fragments;

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

import com.example.fbuapp.FavoritesAdapter;
import com.example.fbuapp.FavoritesViewModel;
import com.example.fbuapp.R;
import com.example.fbuapp.Restaurant;
import com.example.fbuapp.RestaurantsAdapter;
import com.example.fbuapp.RestaurantsViewModel;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment {

    public static final String TAG = "RestaurantsFragment";
    protected RecyclerView rvRestaurants;
    protected List<Restaurant> restaurants;
    protected FavoritesAdapter adapter;

    private FavoritesViewModel mViewModel;
    LinearLayoutManager linearLayoutManager;


    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProviders().of(this).get(FavoritesViewModel.class);

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
        adapter = new FavoritesAdapter(getContext(), restaurants);
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