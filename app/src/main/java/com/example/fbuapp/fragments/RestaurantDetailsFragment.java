package com.example.fbuapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbuapp.R;
import com.example.fbuapp.adapters.FavoritesAdapter;
import com.example.fbuapp.adapters.ReviewsAdapter;
import com.example.fbuapp.models.FavoriteRestaurant;
import com.example.fbuapp.models.Restaurant;
import com.example.fbuapp.models.Review;
import com.example.fbuapp.viewmodels.FavoritesViewModel;
import com.example.fbuapp.viewmodels.ReviewsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;


public class RestaurantDetailsFragment extends Fragment {

    public static final String TAG = "DetailsFragment";

    Context context;
    Restaurant restaurant;

    private TextView tvRestaurant;
    private TextView tvLocation;
    private Button btnFavorites;
    private ImageView ivRestaurantImages;
    private TextView tvMenu;
    private TextView tvReviews;
    private FloatingActionButton btnCreateReview;
    private RecyclerView rvReviews;
    private RatingBar ratingBar;
    private String RESTAURANT_WEBSITE_URL;
    private String restaurantWebsite;
    protected ReviewsAdapter adapter;
    private List<Review> reviews;
    private float rating;
    Matcher m;
    Pattern p = Pattern.compile("www.* *.com*");
    Spanned restaurantHyperlink;
    String htmlText;

    private ReviewsViewModel mViewModel;
    LinearLayoutManager linearLayoutManager;


    public RestaurantDetailsFragment() {
        // Required empty public constructor
    }

    public RestaurantDetailsFragment(Context context, Restaurant restaurant) {
        this.context = context;
        this.restaurant = restaurant;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProviders().of(this).get(ReviewsViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvRestaurant = view.findViewById(R.id.tvRestaurant);
        tvLocation = view.findViewById(R.id.tvLocation);
        btnFavorites = view.findViewById(R.id.btnFavorites);
        ivRestaurantImages = view.findViewById(R.id.ivRestaurantImages);

        tvMenu = view.findViewById(R.id.tvMenu);
        tvMenu.setClickable(true);
        tvMenu.setMovementMethod(LinkMovementMethod.getInstance());

        tvReviews = view.findViewById(R.id.tvReviewTag);
        btnCreateReview = view.findViewById(R.id.btnAddReview);
        rvReviews = view.findViewById(R.id.rvReviews);
        ratingBar = view.findViewById(R.id.rbDetails);

        tvRestaurant.setText(restaurant.getRestaurantName());
        tvLocation.setText(restaurant.getLocation());

        linearLayoutManager = new LinearLayoutManager(getContext());
        rvReviews.setLayoutManager(linearLayoutManager);

        reviews = new ArrayList<>();
        adapter = new ReviewsAdapter(getContext(), reviews);
        rvReviews.setAdapter(adapter);


        mViewModel.getReviews(restaurant).observe(getViewLifecycleOwner(), new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviewsResults) {
                // update UI
                adapter.setReviews(reviewsResults);
                rating = adapter.getRating(reviewsResults);
                ratingBar.setRating(rating);

            }
        });

        getRestaurantWebsite();
        tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("https://" + htmlText));
                startActivity(browserIntent);
            }
        });

        btnCreateReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ComposeReviewFragment(restaurant);
                replaceFragment(fragment);

            }
        });


    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flContainerReview, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private String getRestaurantWebsite() {
        AsyncHttpClient client = new AsyncHttpClient();

        RESTAURANT_WEBSITE_URL = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" +
                restaurant.getRestaurantID() + "&fields=website&key=" + getString(R.string.google_maps_API_key);
        Log.i(TAG, "URL: " + RESTAURANT_WEBSITE_URL);

        client.get(RESTAURANT_WEBSITE_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JsonHttpResponseHandler.JSON json) {
                Log.d(TAG, "onSuccess client request");
                JSONObject jsonObject = json.jsonObject;
                try {
                    restaurantWebsite = jsonObject.getString("result");
                    m = p.matcher(restaurantWebsite);
                    if (m.find()) {
                        htmlText = m.group();
                        restaurantHyperlink = Html.fromHtml("<a href='" + htmlText + "'> " + restaurant.getRestaurantName() + "</a>");
                        //restaurantHyperlink = Html.fromHtml(htmlText);
                        tvMenu.setText(restaurantHyperlink);

                    }
                    Log.i(TAG, String.valueOf(m));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //tvMenu.setText(restaurantWebsite);

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Hit json exception ", throwable);
            }
        });
        return restaurantWebsite;
    }
}