package com.example.moviefinder.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviefinder.databinding.ActivityMovieDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// Activity to display detailed information about a selected movie
public class MovieDetailsActivity extends AppCompatActivity {

    // View binding for layout components
    ActivityMovieDetailsBinding binding;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the IMDb ID of the selected movie
        String movieId = getIntent().getStringExtra("MOVIE_ID");

        // Fetch movie details using the IMDb ID
        if (movieId != null) {
            fetchMovieDetails(movieId);
        }

        // Set up the Back button
        binding.backButton.setOnClickListener(v -> finish());

        if (movieId != null) {
            fetchMovieDetails(movieId);
        }

    }


    private void fetchMovieDetails(String movieId) {
        // URL for fetching movie details from OMDb API
        String url = "https://www.omdbapi.com/?i=" + movieId + "&apikey=c6d84029";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Make a network request to fetch data
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // Parse JSON response and extract details
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        // Extract movie details
                        String title = jsonObject.optString("Title", "N/A");
                        String description = jsonObject.optString("Plot", "No description available.");
                        String year = "Year: " + jsonObject.optString("Year", "Unknown");
                        String rating = "Rating: " + jsonObject.optString("imdbRating", "Not rated");
                        String studio = "Studio: " + jsonObject.optString("Production", "Not available");
                        String genre = "Genre: " + jsonObject.optString("Genre", "Not specified");
                        String collection = "Collection: " + jsonObject.optString("BoxOffice", "No data");
                        String director = "Director: " + jsonObject.optString("Director", "Unknown");
                        String writer = "Written by: " + jsonObject.optString("Writer", "Unknown");
                        String actor = "Actors: " + jsonObject.optString("Actors", "Unknown");
                        String posterUrl = jsonObject.optString("Poster", "");



                        // Update UI components on the main thread
                        runOnUiThread(() -> {
                            binding.txtTitle.setText(title);
                            binding.txtDescription.setText(description);
                            binding.txtYear.setText(year);
                            binding.txtRating.setText(rating);
                            binding.txtGenre.setText(genre);
                            binding.txtBoxOffice.setText(collection);
                            binding.txtStudio.setText(studio);
                            binding.txtDirector.setText(director);
                            binding.txtWriter.setText(writer);
                            binding.txtActor.setText(actor);

                            // Load the poster image using Glide
                            Glide.with(MovieDetailsActivity.this)
                                    .load(posterUrl)
                                    .into(binding.moviePosterImageView);

                            // Initialize Firestore for saving favorite movies
                            firestore = FirebaseFirestore.getInstance();

                            // Set up the favorite button click listener
                            binding.favoriteButton.setOnClickListener(v -> {
                                // Get the current user's ID
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                String userId = auth.getCurrentUser().getUid();

                                // Create a HashMap to represent the movie object
                                HashMap<String, Object> movie = new HashMap<>();
                                movie.put("title", title);
                                movie.put("studio", studio);
                                movie.put("rating", rating);
                                movie.put("posterUrl", posterUrl);
                                movie.put("description", description);
                                movie.put("userId", userId);

                                // Save the movie object to the "favorites" collection in Firestore
                                firestore.collection("favorites").add(movie)
                                        .addOnSuccessListener(documentReference ->
                                                // Show a success message when the movie is added
                                                Toast.makeText(MovieDetailsActivity.this, "Movie added to favorites!", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e ->
                                                // Show an error message if adding fails
                                                Toast.makeText(MovieDetailsActivity.this, "Failed to add movie.", Toast.LENGTH_SHORT).show());
                            });
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
