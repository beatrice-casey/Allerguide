package com.example.fbuapp.adapters;

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

import com.example.fbuapp.R;
import com.example.fbuapp.models.Favorite;
import com.example.fbuapp.models.FavoriteRestaurant;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private Context context;
    protected List<FavoriteRestaurant> restaurants;
    public static final String TAG = "RestaurantsAdapter";
    private boolean isFavorite;
    private Favorite favorite;


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

        public void bind(FavoriteRestaurant restaurant) {
            //Log.i(TAG, "binding data");
            tvRestaurant.setText(restaurant.getRestaurantNameFromParse());
            //tvLocation.setText(restaurant.getLocation());
//            Glide.with(context)
//                    .load(restaurant.getImage())
//                    .into(ivRestaurantImage);
            btnFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_24);


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

        }
    }
}


