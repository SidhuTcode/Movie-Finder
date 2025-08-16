package com.example.moviefinder.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.moviefinder.R;
import com.example.moviefinder.databinding.ActivityFavoriteMoviesBinding;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteMoviesActivity extends AppCompatActivity {

    ActivityFavoriteMoviesBinding binding; // Binding for layout elements
    FirebaseFirestore firestore; // Firestore database instance
    FavoriteMovieAdapter adapter; // RecyclerView adapter
    List<Map<String, Object>> favoriteMovies = new ArrayList<>(); // List to hold favorite movies
    ListenerRegistration listenerRegistration; // Firestore listener to fetch live updates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout and set up view binding
        binding = ActivityFavoriteMoviesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up RecyclerView with a LinearLayoutManager
        RecyclerView recyclerView = findViewById(R.id.favMoviesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firestore instance
        firestore = FirebaseFirestore.getInstance();

        // Set up the adapter for RecyclerView with a click listener for each movie item
        adapter = new FavoriteMovieAdapter(favoriteMovies, movie -> {
            // Navigate to FavoriteDetailsActivity and pass movie data
            Intent intent = new Intent(FavoriteMoviesActivity.this, FavoriteDetailsActivity.class);
            intent.putExtra("movie", new HashMap<>(movie));
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter); // Attach the adapter to RecyclerView

        // Load the user's favorite movies from Firestore
        loadFavorites();

        // Handle navigation via BottomNavigationView
        binding.bottomNavigationMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeID:
                    // Navigate to the MainActivity
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // Reuse the existing activity
                    startActivity(intent);
                    break;

                case R.id.favoritesID:
                    // Stay on this activity (no action required)
                    break;
            }
            return false;
        });
    }


     //Fetches and displays the user's favorite movies from Firestore.

    private void loadFavorites() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user's UID

        // Set up a Firestore listener to fetch favorite movies for the current user
        listenerRegistration = firestore.collection("favorites")
                .whereEqualTo("userId", userId) // Filter by userId
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Show a toast if an error occurs while fetching data
                        Toast.makeText(this, "Error loading data.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Clear the existing list and populate it with new data from Firestore
                    favoriteMovies.clear();
                    for (var doc : value.getDocuments()) {
                        Map<String, Object> movie = doc.getData();
                        movie.put("id", doc.getId()); // Add document ID to movie data
                        favoriteMovies.add(movie);
                    }
                    adapter.notifyDataSetChanged(); // Notify adapter about data changes
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) {
            // Remove Firestore listener to prevent memory leaks
            listenerRegistration.remove();
        }
    }
}