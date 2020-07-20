package com.example.fbuapp;

import android.content.Context;
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
import com.bumptech.glide.request.target.Target;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * This is the adapter that binds the data from the view model to the view. It also updates the view based on new data from the view model.
 */

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private Context context;
    private List<Restaurant> restaurants;
    public static final String TAG = "RestaurantsAdapter";
    private boolean isFavorite;
    private Favorite favorite;

    public RestaurantsAdapter(Context context, List<Restaurant> restaurants) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder " + position);
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Button btnFavorites;
        private TextView tvRestaurant;
        private TextView tvLocation;
        private ImageView ivRestaurantImage;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnFavorites = itemView.findViewById(R.id.btnFavorites);
            tvRestaurant = itemView.findViewById(R.id.tvRestaurant);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            ivRestaurantImage = itemView.findViewById(R.id.ivRestaurantImage);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            favorite = new Favorite();
            Log.i(TAG, "Setting up elements");

            btnFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkFavorite();
                    if (!isFavorite) {
                        isFavorite = true;
                        btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                        restaurants.get(getAdapterPosition()).setRestaurant(tvRestaurant.getText().toString());
                        favorite = restaurants.get(getAdapterPosition()).saveFavorite(ParseUser.getCurrentUser(), restaurants.get(getAdapterPosition()));
                        restaurants.get(getAdapterPosition()).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Error saving restaurant", e);
                                    return;
                                }
                            }
                        });

                    }
                    else {
                        isFavorite = false;
                        btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                        restaurants.get(getAdapterPosition()).deleteFavorite(favorite, restaurants.get(getAdapterPosition()));
                        restaurants.get(getAdapterPosition()).saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Error saving restaurant", e);
                                    return;
                                }
                            }
                        });
                    }

                }
            });
        }

        @Override
        public void onClick(View view) {

        }

        public void bind(Restaurant restaurant) {
            Log.i(TAG, "binding data");
            tvRestaurant.setText(restaurant.getRestaurantName());
            tvLocation.setText(restaurant.getLocation());
            Glide.with(context)
                    .load(restaurant.getImage())
                    .into(ivRestaurantImage);
            //Log.i(TAG, "binding data");
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
    }


}
