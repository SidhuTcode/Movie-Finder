package com.example.moviefinder.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviefinder.databinding.ActivityFavoriteDetailsBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FavoriteDetailsActivity extends AppCompatActivity {

    // Firebase Firestore instance for database operations
    FirebaseFirestore firestore;

    // Binding object for accessing UI elements
    ActivityFavoriteDetailsBinding binding;

    // Map to hold the movie data passed from the previous activity
    Map<String, Object> movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding to access views
        binding = ActivityFavoriteDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore instance
        firestore = FirebaseFirestore.getInstance();

        // Retrieve the movie object passed from the previous activity
        movie = (HashMap<String, Object>) getIntent().getSerializableExtra("movie");
        int position = getIntent().getIntExtra("position", -1); // Retrieve the position if needed

        // Set the movie details into the UI elements
        binding.txtTitle.setText((String) movie.get("title"));
        binding.txtRating.setText((String) movie.get("rating"));
        binding.txtDescription.setText((String) movie.get("description"));

        // Use Glide to load and display the movie poster image
        Glide.with(FavoriteDetailsActivity.this)
                .load(movie.get("posterUrl"))
                .into(binding.moviePosterImageView);

        // Set click listener for delete button
        binding.btnDelete.setOnClickListener(v -> deleteMovie((String) movie.get("id")));

        // Set click listener for update button
        binding.btnUpdate.setOnClickListener(v -> updateMovie((String) movie.get("id")));

        // Set click listener for back button to finish the activity
        binding.backButton.setOnClickListener(v -> finish());
    }

    // Method to delete a movie from Firestore using its ID
    private void deleteMovie(String id) {
        firestore.collection("favorites").document(id).delete() // Delete the document in Firestore
                .addOnSuccessListener(aVoid -> {
                    // Show a toast message and finish the activity if deletion is successful
                    Toast.makeText(this, "Movie deleted", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    // Method to update a movie's description in Firestore
    private void updateMovie(String id) {
        // Create a map of updated data
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("description", binding.txtDescription.getText().toString()); // Update description

        // Update the movie document in Firestore with the new data
        firestore.collection("favorites").document(id).update(updatedData)
                .addOnSuccessListener(aVoid ->
                        // Show a toast message when the update is successful
                        Toast.makeText(this, "Movie updated", Toast.LENGTH_SHORT).show());
    }

}

