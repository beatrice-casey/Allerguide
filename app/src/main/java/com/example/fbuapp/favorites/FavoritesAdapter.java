package com.example.fbuapp.favorites;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuapp.R;
import com.example.fbuapp.details.RestaurantDetailsActivity;
import com.example.fbuapp.models.Favorite;
import com.example.fbuapp.models.FavoriteRestaurant;
import com.example.fbuapp.models.Review;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;


/**
 * This class is the adapter that binds the data received from the Parse database to the view. This
 * adapter is for the Favorites tab, which shows all of the user's favorite restaurants.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private Context context;
    protected List<FavoriteRestaurant> restaurants;
    public static final String TAG = "RestaurantsAdapter";
    private boolean isFavorite;
    private Favorite favorite;
    float rating;
    private String RESTAURANT_PHOTO_URL;


    public FavoritesAdapter(Context context, List<FavoriteRestaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View restaurantView = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(restaurantView);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void setRestaurants(List<FavoriteRestaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteRestaurant restaurant = restaurants.get(position);

        holder.bind(restaurant);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected Button btnFavorites;
        protected TextView tvRestaurant;
        protected TextView tvLocation;
        protected ImageView ivRestaurantImage;
        protected RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnFavorites = itemView.findViewById(R.id.btnFavorites);
            tvRestaurant = itemView.findViewById(R.id.tvRestaurant);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            ivRestaurantImage = itemView.findViewById(R.id.ivRestaurantImage);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            favorite = new Favorite();
            Log.i(TAG, "Setting up elements");
            //itemView.setOnClickListener(this);

            btnFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkFavorite();
                    if (!isFavorite) {

                    } else {
                        isFavorite = false;
                        btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                        //restaurants.get(getAdapterPosition()).deleteFavorite(favorite, restaurants.get(getAdapterPosition()));
                    }

                }
            });

        }

        private void bind(FavoriteRestaurant restaurant) {
            //Log.i(TAG, "binding data");
            tvRestaurant.setText(restaurant.getRestaurantNameFromParse());
            tvLocation.setText(restaurant.getLocation());
            if(restaurant.getImage() != null) {
                RESTAURANT_PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + restaurant.getImage() +
                        "&key=" + context.getString(R.string.google_maps_API_key);
                Glide.with(context)
                        .load(RESTAURANT_PHOTO_URL)
                        .into(ivRestaurantImage);
            }
            btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
            getRestaurantRating(restaurant);


        }

        private float getRestaurantRating(FavoriteRestaurant restaurant) {
            rating = 0;
            //Specify which class to query
            ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
            //get the user who's review it is
            query.include(Review.KEY_RESTAURANT);
            query.include(Review.KEY_RESTAURANT_NAME);
            query.whereEqualTo(Review.KEY_RESTAURANT_NAME, restaurant.getRestaurantNameFromParse());
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
                            Log.i(TAG, "Rating is: " + rating);
                        }
                        rating = rating/parseReviews.size();
                        ratingBar.setRating(rating);
                    }
                }
            });

            return rating;
        }

        private void checkFavorite() {
            ParseQuery<Favorite> query = ParseQuery.getQuery(Favorite.class);
            query.whereEqualTo(Favorite.KEY_USER, ParseUser.getCurrentUser());
            query.whereEqualTo(Favorite.KEY_RESTAURANT, restaurants.get(getAdapterPosition()));
            query.findInBackground(new FindCallback<Favorite>() {
                @Override
                public void done(List<Favorite> favorites, ParseException e) {
                    if (e != null) {
                        return;
                    }
                    if (favorites.isEmpty()) {
                        isFavorite = false;
                    } else {
                        isFavorite = true;
                        favorite = favorites.get(0);
                    }

                }
            });

        }

        @Override
        public void onClick(View view) {

            //getting adapter position
            int position = getAdapterPosition();
            //make sure position is valid (it exists in view)
            if (position != RecyclerView.NO_POSITION) {
                //get the movie at that position
                FavoriteRestaurant restaurant = restaurants.get(position);
                Log.i(TAG, "Restaurant is:" + restaurant.getRestaurantNameFromParse());
                //make an intent to display RestaurantDetails
                Intent intent = new Intent(context, RestaurantDetailsActivity.class);
                //serialize the restaurant using parceler, use short name as key
                intent.putExtra(FavoriteRestaurant.class.getSimpleName(), Parcels.wrap(restaurant));
                //intent.putExtra("restaurant", restaurant);
                //show the activity
                context.startActivity(intent);
            }

        }
    }
}


