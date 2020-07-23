package com.example.fbuapp.adapters;

import android.content.Context;
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
import com.example.fbuapp.models.Favorite;
import com.example.fbuapp.models.FavoriteRestaurant;
import com.example.fbuapp.models.Review;
import com.parse.ParseFile;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context context;
    protected List<Review> reviews;
    public static final String TAG = "RestaurantsAdapter";

    public ReviewsAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View reviewView = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.bind(review);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public float getRating(List<Review> reviews) {
        int i;
        float rating = 0;
        for (i = 0; i < reviews.size(); i ++) {
            rating += reviews.get(i).getRating();
        }
        rating = rating/reviews.size();
        return rating;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        protected TextView tvUsername;
        protected TextView tvDescription;
        protected ImageView ivReviewImage;
        protected RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivReviewImage = itemView.findViewById(R.id.ivReviewPhoto);
            ratingBar = itemView.findViewById(R.id.rbReview);

        }


        public void bind(Review review) {

            tvUsername.setText(review.getUser().getUsername());
            tvDescription.setText(review.getDescription());
            ratingBar.setRating(review.getRating());

            ParseFile image = review.getImage();
            if (image != null) {
                Glide.with(context).load(review.getImage().getUrl()).into(ivReviewImage);
            }
        }
    }
}
