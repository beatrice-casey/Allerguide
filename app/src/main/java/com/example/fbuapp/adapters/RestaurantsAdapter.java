package com.example.fbuapp.adapters;

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
import com.example.fbuapp.activities.RestaurantDetailsActivity;
import com.example.fbuapp.models.Favorite;
import com.example.fbuapp.models.Restaurant;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

/**
 * This is the adapter that binds the data from the view model to the view. It also updates the view based on new data from the view model.
 */

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private Context context;
    protected List<Restaurant> restaurants;
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            itemView.setOnClickListener(this);

            btnFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkFavorite();
                    if (!isFavorite) {
                        isFavorite = true;
                        btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                        restaurants.get(getAdapterPosition()).setRestaurant(tvRestaurant.getText().toString());
                        restaurants.get(getAdapterPosition()).saveFavorite(ParseUser.getCurrentUser(), restaurants.get(getAdapterPosition()));

                    }
                    else {
                        isFavorite = false;
                        btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                        restaurants.get(getAdapterPosition()).deleteFavorite(favorite, restaurants.get(getAdapterPosition()));
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
            Glide.with(context)
                    .load(restaurant.getImage())
                    .into(ivRestaurantImage);
            checkFavorite();
            if (isFavorite) {
                btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
            }
            else {
                btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
            }


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
