package com.example.moviefinder.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.moviefinder.R;


import java.util.List;
import java.util.Map;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteViewHolder> {

    // List of movies, each represented as a map of key-value pairs (e.g., title, rating)
    private List<Map<String, Object>> movieList;

    // Listener interface for handling item click events
    private final OnItemClickListener listener;

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(Map<String, Object> movie); // Called when a movie is clicked
    }

    // Constructor for initializing the adapter with a movie list and listener
    public FavoriteMovieAdapter(List<Map<String, Object>> movieList, OnItemClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    // Inflates the layout for each item in the RecyclerView
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_movie, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    // Binds data to each ViewHolder for display
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Map<String, Object> movie = movieList.get(position); // Get movie at the current position
        holder.bind(movie, listener); // Bind data and click listener to the ViewHolder
    }

    @Override
    // Returns the total number of items in the movie list
    public int getItemCount() {
        return movieList.size();
    }

    // Method to remove an item from the movie list and update the RecyclerView
    public void removeItem(int position) {
        if (position >= 0 && position < movieList.size()) {
            movieList.remove(position); // Remove the movie from the list
            notifyItemRemoved(position); // Notify RecyclerView about item removal
            notifyItemRangeChanged(position, movieList.size()); // Update remaining items
        }
    }

    // ViewHolder class for managing individual movie items in the RecyclerView
    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        // UI elements for displaying movie details
        TextView txtTitle, txtStudio, txtRating;
        ImageView imgPoster;

        // Constructor for initializing UI elements
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtMovie); // Movie title
            txtStudio = itemView.findViewById(R.id.txtStudio); // Movie studio
            txtRating = itemView.findViewById(R.id.txtRating); // Movie rating
            imgPoster = itemView.findViewById(R.id.movieThumbnail); // Movie poster
        }

        // Method to bind movie data to UI elements and set the click listener
        public void bind(Map<String, Object> movie, OnItemClickListener listener) {
            txtTitle.setText((String) movie.get("title")); // Set movie title
            txtStudio.setText((String) movie.get("studio")); // Set movie studio
            txtRating.setText((String) movie.get("rating")); // Set movie rating

            // Load the movie poster image using Glide library
            Glide.with(imgPoster.getContext())
                    .load((String) movie.get("posterUrl")) // Poster URL
                    .into(imgPoster);

            // Set the click listener for the entire item view
            itemView.setOnClickListener(v -> listener.onItemClick(movie));
        }
    }
}











