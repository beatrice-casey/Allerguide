package com.example.fbuapp;


import android.app.Application;

import androidx.arch.core.executor.TaskExecutor;
import androidx.lifecycle.Observer;

import com.example.fbuapp.home.MainActivity;
import com.example.fbuapp.home.RestaurantsAdapter;
import com.example.fbuapp.home.RestaurantsFragment;
import com.example.fbuapp.home.RestaurantsViewModel;
import com.example.fbuapp.models.Restaurant;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.model.TestClass;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@RunWith(JUnit4.class)
public class restaurantsTest {

    private RestaurantsFragment restaurantsFragment;

    @Mock
    private RestaurantsViewModel restaurantsViewModel;

    @Mock
    private RestaurantsAdapter restaurantsAdapter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        restaurantsFragment = Mockito.spy(new RestaurantsFragment());
    }

    @Test
    public void searchRestaurants_noQuery() {
        String searchQuery = null;


    }


}
