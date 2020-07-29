package com.example.fbuapp.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuapp.R;
import com.example.fbuapp.details.RestaurantDetailsActivity;
import com.example.fbuapp.models.Favorite;
import com.example.fbuapp.models.FavoriteRestaurant;
import com.example.fbuapp.models.Restaurant;
import com.example.fbuapp.models.Review;
import com.example.fbuapp.models.Tags;
import com.example.fbuapp.tags.TagsViewModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

/**
 * This is the adapter that binds the data from the view model to the view. It also updates the view based on new data from the view model.
 * Additionally, it listens for a double tap or a click on the heart button (btnFavorites) and adds the restaurant to the Parse database and
 * to the user's favorites tab.
 */

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private Context context;
    protected List<Restaurant> restaurants;
    private Fragment fragment;
    public static final String TAG = "RestaurantsAdapter";

    private Favorite favorite;
    private FavoriteRestaurant newFavoriteRestaurant;
    private FavoriteRestaurant favoriteRestaurant;


    public RestaurantsAdapter(Context context, List<Restaurant> restaurants, Fragment fragment) {
        this.context = context;
        this.restaurants = restaurants;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View restaurantView = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(restaurantView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Log.i(TAG, "onBindViewHolder " + position);
        Restaurant restaurant = restaurants.get(position);
        holder.bind(restaurant);

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    public void addAll(List<Restaurant> newRestaurants) {
        restaurants.addAll(newRestaurants);
        notifyDataSetChanged();
    }

    public void clear() {
        restaurants.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected Button btnFavorites;
        protected TextView tvRestaurant;
        protected TextView tvLocation;
        protected ImageView ivRestaurantImage;
        protected RatingBar ratingBar;
        protected TextView tvTags;
        private boolean isFavorite;
        private float rating;
        private String RESTAURANT_PHOTO_URL;
        private String tagsString;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            btnFavorites = itemView.findViewById(R.id.btnFavorites);
            tvRestaurant = itemView.findViewById(R.id.tvRestaurant);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            ivRestaurantImage = itemView.findViewById(R.id.ivRestaurantImage);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvTags = itemView.findViewById(R.id.tvTags);
            favorite = new Favorite();
            newFavoriteRestaurant = new FavoriteRestaurant();
            favoriteRestaurant = new FavoriteRestaurant();

            itemView.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                public boolean onDoubleTap(MotionEvent e) {
                    favoriteRestaurant = addRestaurantToFavorites(restaurants.get(getAdapterPosition()));
                    btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                    return super.onDoubleTap(e);
                }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return false;
                }
            });
            itemView.setOnClickListener(this);



            btnFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isFavorite) {
                        favoriteRestaurant = addRestaurantToFavorites(restaurants.get(getAdapterPosition()));
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

        }

        private FavoriteRestaurant addRestaurantToFavorites(Restaurant restaurant) {
            favoriteRestaurant = newFavoriteRestaurant.saveRestaurant(restaurant, ParseUser.getCurrentUser());
            favorite = newFavoriteRestaurant.saveFavorite(ParseUser.getCurrentUser(), favoriteRestaurant);
            isFavorite = true;
            return favoriteRestaurant;

        }

        @Override
        public void onClick(View view) {
            //getting adapter position
            int position = getAdapterPosition();
            //make sure position is valid (it exists in view)
            if (position != RecyclerView.NO_POSITION) {
                //get the movie at that position
                Restaurant restaurant = restaurants.get(position);
                Log.i(TAG, "Restaurant is:" + restaurant.getRestaurantName());
                //make an intent to display RestaurantDetails
                Intent intent = new Intent(context, RestaurantDetailsActivity.class);
                //serialize the restaurant using parceler, use short name as key
                intent.putExtra(Restaurant.class.getSimpleName(), Parcels.wrap(restaurant));
                //intent.putExtra("restaurant", restaurant);
                //show the activity
                context.startActivity(intent);
            }

        }

        public void bind(Restaurant restaurant) {
            Log.i(TAG, "binding data");
            tvRestaurant.setText(restaurant.getRestaurantName());
            tvLocation.setText(restaurant.getLocation());
            checkFavorite(restaurant);
            Log.i(TAG, "The rating before setting it is: " + rating);
            getRestaurantRating(restaurant);
            getRestaurantPhoto(restaurant);
            getRestaurantTags(restaurant);

        }

        private void getRestaurantTags(Restaurant restaurant) {
            tagsString = "";
            //Specify which class to query
            ParseQuery<Tags> query = ParseQuery.getQuery(Tags.class);
            query.include(Tags.KEY_RESTAURANT_NAME);
            query.whereEqualTo(Tags.KEY_RESTAURANT_NAME, restaurant.getRestaurantName());
            query.findInBackground(new FindCallback<Tags>() {
                @Override
                public void done(List<Tags> tags, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with getting allergies", e);
                        return;
                    }
                    for (Tags tag : tags) {
                        Log.i(TAG, "Vegan: " + tag.getVegan() + " Vegetarian: " + tag.getVegetarian()
                                + " GF: " + tag.getGlutenFree() + " LF: " + tag.getLactoseFree());
                    }
                    if (!tags.isEmpty()) {

                        if (tags.get(0).getVegan()) {
                            tagsString += "Vegan ";
                        }
                        if (tags.get(0).getVegetarian()) {
                            tagsString += "Vegetarian ";
                        }
                        if (tags.get(0).getGlutenFree()) {
                            tagsString += "GF ";
                        }
                        if (tags.get(0).getLactoseFree()) {
                            tagsString += "Lactose Free ";
                        }

                    }
                    tvTags.setText(tagsString);

                }

            });
        }

        private void getRestaurantRating(Restaurant restaurant) {
            rating = 0;
            //Specify which class to query
            ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
            //get the user who's review it is
            query.include(Review.KEY_RESTAURANT);
            query.include(Review.KEY_RESTAURANT_NAME);
            query.whereEqualTo(Review.KEY_RESTAURANT_NAME, restaurant.getRestaurantName());
            query.whereEqualTo(Review.KEY_LOCATION, restaurant.getLocation());
            query.addDescendingOrder(Review.KEY_CREATED);
            query.findInBackground(new FindCallback<Review>() {
                @Override
                public void done(List<Review> parseReviews, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with getting reviews", e);
                        return;
                    }
                    if (parseReviews.size() != 0) {
                        int i;
                        for (i = 0; i < parseReviews.size(); i ++) {
                            rating += parseReviews.get(i).getRating();
                        }
                        rating = (rating/parseReviews.size());
                        ratingBar.setRating(rating);
                    }
                }
            });
        }

        private boolean checkFavorite(Restaurant restaurant) {
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
                        favorite = favorites.get(0);
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
            return isFavorite;

        }

        private void getRestaurantPhoto(Restaurant restaurant) {
            if (restaurant.getPhotoID() != null) {
                RESTAURANT_PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + restaurant.getPhotoID() +
                        "&key=" + context.getString(R.string.google_maps_API_key);
                Glide.with(context).load(RESTAURANT_PHOTO_URL).into(ivRestaurantImage);
            }

        }
    }

}
