package com.example.fbuapp.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbuapp.R;
import com.example.fbuapp.models.Favorite;
import com.example.fbuapp.models.FavoriteRestaurant;
import com.example.fbuapp.models.Restaurant;
import com.example.fbuapp.models.Review;
import com.example.fbuapp.tags.TagsFragment;
import com.example.fbuapp.tags.TagsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;

/**
 * This class sets up the view for a detailed view of a specific restaurant. It uses a view model to handle getting a list of reviews from parse.
 * This class also gets the link to the restaurant's website and creates a link that the user can click to go to that website. It also gets a photo
 * preview from the restaurant (a photo of food that the restaurant has posted). Finally, it sets up the button to allow the user to make a review.
 * When that button is clicked, the ComposeReviewFragment is started so the user can make their review.
 */

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
    private Matcher m;
    private Pattern p = Pattern.compile("www.* *.com*");
    private Spanned restaurantHyperlink;
    private String htmlText;
    private boolean isFavorite;
    private Favorite favorite;
    private FavoriteRestaurant newFavoriteRestaurant;
    private FavoriteRestaurant favoriteRestaurant;
    private TextView tvEmptyReviewsNote;
    private Button btnAddTag;
    private TextView tvTags;


    private String RESTAURANT_PHOTO_URL;

    private ReviewsViewModel mViewModel;
    LinearLayoutManager linearLayoutManager;

    private TagsViewModel tagsViewModel;
    private String tags;


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
        tagsViewModel = new ViewModelProviders().of(this).get(TagsViewModel.class);

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

        tvEmptyReviewsNote = view.findViewById(R.id.tvEmptyTextNote);
        tvReviews = view.findViewById(R.id.tvReviewTag);
        btnCreateReview = view.findViewById(R.id.btnAddReview);
        rvReviews = view.findViewById(R.id.rvReviews);
        ratingBar = view.findViewById(R.id.rbDetails);
        btnAddTag = view.findViewById(R.id.btnAddTags);
        tvTags = view.findViewById(R.id.tvTags);

        tvRestaurant.setText(restaurant.getRestaurantName());
        tvLocation.setText(restaurant.getLocation());

        linearLayoutManager = new LinearLayoutManager(getContext());
        rvReviews.setLayoutManager(linearLayoutManager);

        reviews = new ArrayList<>();
        adapter = new ReviewsAdapter(getContext(), reviews);
        rvReviews.setAdapter(adapter);

        favorite = new Favorite();
        newFavoriteRestaurant = new FavoriteRestaurant();
        favoriteRestaurant = new FavoriteRestaurant();

        getRestaurantPhoto();

        tvEmptyReviewsNote.setText("There are no reviews for this restaurant yet");

        mViewModel.getReviews(restaurant).observe(getViewLifecycleOwner(), new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviewsResults) {
                // update UI

                tvEmptyReviewsNote.setText("");
                adapter.setReviews(reviewsResults);
                rating = adapter.getRating(reviewsResults);
                ratingBar.setRating(rating);


            }
        });
        checkFavorite(restaurant);

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

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavorite) {
                    favoriteRestaurant = addRestaurantToFavorites(restaurant);
                    Log.i(TAG, "Restaurant that is saved is: " + favoriteRestaurant);
                    btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_24);

                }
                else {
                    isFavorite = false;
                    btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                    Log.i(TAG, "Restaurant to delete: " + favoriteRestaurant.getRestaurantNameFromParse());
                    newFavoriteRestaurant.deleteFavorite(favorite, favoriteRestaurant);
                }

            }
        });
        
        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTags();
            }
        });

        tagsViewModel.getTags(restaurant).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvTags.setText(s);
            }
        });


    }

    private void addTags() {
        Fragment fragment = new TagsFragment(restaurant);
        replaceFragment(fragment);
    }

    private FavoriteRestaurant addRestaurantToFavorites(Restaurant restaurant) {
        favoriteRestaurant = newFavoriteRestaurant.saveRestaurant(restaurant, ParseUser.getCurrentUser());
        favorite = newFavoriteRestaurant.saveFavorite(ParseUser.getCurrentUser(), favoriteRestaurant);
        isFavorite = true;
        return favoriteRestaurant;

    }

    private void getRestaurantPhoto() {
        if (restaurant.getPhotoID() != null) {
            RESTAURANT_PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + restaurant.getPhotoID() +
                    "&key=" + getString(R.string.google_maps_API_key);
            Glide.with(context).load(RESTAURANT_PHOTO_URL).into(ivRestaurantImages);
        }

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
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

    private void checkFavorite(Restaurant restaurant) {
        ParseQuery<Favorite> query = ParseQuery.getQuery(Favorite.class);
        query.include(Favorite.KEY_RESTAURANT_NAME);
        query.include(Favorite.KEY_RESTAURANT);
        query.whereEqualTo(Favorite.KEY_USER, ParseUser.getCurrentUser());
        query.whereEqualTo(Favorite.KEY_RESTAURANT_NAME, restaurant.getRestaurantName());
        query.findInBackground(new FindCallback<Favorite>() {
            @Override
            public void done(List<Favorite> favorites, ParseException e) {
                if (e != null) {
                    return;
                }
                if (favorites.isEmpty()) {
                    isFavorite = false;
                    //btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                } else {
                    isFavorite = true;
                    //favorite = favorites.get(0);
                    Log.i(TAG, "This is the restaurant that was favorited: " + favorites.get(0).getRestaurantNameFromParse());
                    //btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                }
                if (isFavorite) {
                    btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                }
                else {
                    btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                }

            }

        });

    }
}