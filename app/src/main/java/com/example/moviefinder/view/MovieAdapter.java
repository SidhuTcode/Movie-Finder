package com.example.moviefinder.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviefinder.R;
import com.example.moviefinder.model.Movie;

import java.util.List;

// Adapter to manage and display movie data in RecyclerView
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    // List of movies to display
    List<Movie> movieList;

    // Constructor to initialize movie list
    MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each movie item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        // Bind movie data to the UI components
        Movie movie = movieList.get(position);
        holder.titleTextView.setText(movie.getTitle());
        holder.studioTextView.setText(movie.getStudio());
        holder.ratingTextView.setText(movie.getRating());
        holder.yearTextView.setText(movie.getYear());

        // Set click listener to navigate to MovieDetailsActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MovieDetailsActivity.class);
            intent.putExtra("MOVIE_ID", movie.getImdbId()); // Pass the IMDb ID
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Return the number of movies in the list
        return movieList != null ? movieList.size() : 0;
    }

    // ViewHolder class for movie item layout
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, studioTextView, ratingTextView, yearTextView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            // Initialize UI components
            titleTextView = itemView.findViewById(R.id.movieTitle);
            studioTextView = itemView.findViewById(R.id.movieStudio);
            ratingTextView = itemView.findViewById(R.id.movieRating);
            yearTextView = itemView.findViewById(R.id.movieYear);
        }
    }

    // Method to update the movie list and refresh RecyclerView
    public void updateMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }
}
