package com.example.moviefinder.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviefinder.R;
import com.example.moviefinder.databinding.ActivityMainBinding;
import com.example.moviefinder.model.Movie;
import com.example.moviefinder.viewmodel.MovieViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // View binding for layout components
    ActivityMainBinding binding;

    // Adapter to display movie list in RecyclerView
    MovieAdapter movieAdapter;

    // ViewModel for handling data
    MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up RecyclerView to display movies
        binding.moviesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(null);
        binding.moviesRecyclerView.setAdapter(movieAdapter);

        // Initialize the ViewModel
        movieViewModel = new MovieViewModel();

        // Observe changes in the movie list from the ViewModel
        movieViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                // Update the RecyclerView when data changes
                movieAdapter.updateMovieList(movies);
            }
        });

        // Set up search button functionality
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the movie name from the search bar
                String movieName = binding.searchEditText.getText().toString();

                // Validate input and trigger the search
                if (!movieName.isEmpty()) {
                    movieViewModel.searchMovies(movieName);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a movie name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle navigation
        binding.bottomNavigationMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeID:
                    // Stay on the current activity
                    break;

                case R.id.favoritesID:
                    // Bring FavoriteMoviesActivity to the front if it already exists
                    Intent intent = new Intent(this, FavoriteMoviesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // Reuse the existing activity
                    startActivity(intent);
                    break;
            }
            return false;
        });
    }
}
