package com.example.fbuapp.favorites;

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
import android.widget.TextView;

import com.example.fbuapp.models.FavoriteRestaurant;
import com.example.fbuapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This class sets the view for the favorites tab, which shows which restaurants are the user's favorite
 * It uses a view model to get an updated list of favorite restaurants to pass to the adapter.
 */

public class FavoritesFragment extends Fragment {

    public static final String TAG = "RestaurantsFragment";
    protected RecyclerView rvRestaurants;
    protected List<FavoriteRestaurant> restaurants;
    protected FavoritesAdapter adapter;
    private TextView tvEmptyFavorites;

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
        tvEmptyFavorites = view.findViewById(R.id.tvEmptyFavorites);
        rvRestaurants = view.findViewById(R.id.rvRestaurants);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvRestaurants.setLayoutManager(linearLayoutManager);

        restaurants = new ArrayList<>();
        adapter = new FavoritesAdapter(getContext(), restaurants);
        rvRestaurants.setAdapter(adapter);
        tvEmptyFavorites.setText("You have no favorites yet! Click the heart next to a restaurant to add them to this tab");


        mViewModel.getRestaurants().observe(getViewLifecycleOwner(), new Observer<List<FavoriteRestaurant>>() {
            @Override
            public void onChanged(List<FavoriteRestaurant> restaurants) {
                // update UI
                if(!restaurants.isEmpty()) {
                    tvEmptyFavorites.setText("");
                    adapter.setRestaurants(restaurants);
                } else {
                    tvEmptyFavorites.setText("You have no favorites yet! Click the heart next to a restaurant to add it to this tab");
                }

            }
        });


    }

}