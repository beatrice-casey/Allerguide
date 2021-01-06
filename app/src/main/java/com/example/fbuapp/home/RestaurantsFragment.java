package com.example.fbuapp.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fbuapp.EndlessRecyclerViewScrollListener;
import com.example.fbuapp.R;
import com.example.fbuapp.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

/**
 * This class is the fragment that shows the user the list of restaurants near them. This class handles ONLY the view.
 */

public class RestaurantsFragment extends Fragment {

    public static final String TAG = "RestaurantsFragment";
    private RecyclerView rvRestaurants;
    private List<Restaurant> restaurants;
    private RestaurantsAdapter adapter;

    private RestaurantsViewModel mViewModel;
    LinearLayoutManager linearLayoutManager;

    protected SwipeRefreshLayout swipeContainer;
    protected EndlessRecyclerViewScrollListener scrollListener;

    private ProgressBar progressBar;


    public RestaurantsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

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
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        restaurants = new ArrayList<>();
        adapter = new RestaurantsAdapter(getContext(), restaurants, this);
        rvRestaurants.setAdapter(adapter);
        mViewModel = new ViewModelProviders().of(this).get(RestaurantsViewModel.class);


        mViewModel.getRestaurants().observe(getViewLifecycleOwner(), new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {
                // update UI
                progressBar.setVisibility(View.GONE);
                adapter.setRestaurants(restaurants);
            }
        });

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                mViewModel.getRestaurants().observe(getViewLifecycleOwner(), new Observer<List<Restaurant>>() {
                    @Override
                    public void onChanged(List<Restaurant> restaurants) {
                        // update UI
                        adapter.setRestaurants(restaurants);
                    }
                });
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Stop animation (This will be after 3 seconds)
                                swipeContainer.setRefreshing(false);
                            }
                        }, 4000); // Delay in millis

                    }
                });
                // Configure the refreshing colors
                swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark,
                        android.R.color.holo_green_dark,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_dark);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMoreData();
            }
        };
        // Adds the scroll listener to RecyclerView
        rvRestaurants.addOnScrollListener(scrollListener);
            }

    private void loadMoreData() {
        mViewModel.loadMoreRestaurants().observe(getViewLifecycleOwner(), new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {
                // update UI
                adapter.addAll(restaurants);
            }
        });

    }


}

