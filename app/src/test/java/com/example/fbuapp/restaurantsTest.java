package com.example.fbuapp;


import com.example.allerguide.home.RestaurantsAdapter;
import com.example.allerguide.home.RestaurantsFragment;
import com.example.allerguide.home.RestaurantsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
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
