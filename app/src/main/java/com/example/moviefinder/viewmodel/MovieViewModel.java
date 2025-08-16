package com.example.moviefinder.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviefinder.model.Movie;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// ViewModel to handle movie data and provide it to the UI
public class MovieViewModel extends ViewModel {

    // MutableLiveData to store and update the list of movies
    final MutableLiveData<List<Movie>> movies;

    // Constructor initializes the LiveData
    public MovieViewModel() {
        movies = new MutableLiveData<>();
    }

    // Exposes the LiveData for observing movie data from the UI
    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    // Searches for movies using the OMDb API based on the entered movie name
    public void searchMovies(String movieName) {
        // Build the URL for the OMDb API search endpoint
        String url = "https://www.omdbapi.com/?s=" + movieName + "&apikey=c6d84029";

        // Create an OkHttpClient for making network requests
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Make a network call to search for movies
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                // Log an error if the network request fails
                Log.e("API_ERROR", "Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // Parse the JSON response from the server
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        // Extract the "Search" array from the JSON response
                        JSONArray moviesArray = jsonObject.getJSONArray("Search");

                        // Create a list to hold Movie objects
                        List<Movie> movieList = new ArrayList<>();

                        // Loop through the array and process each movie
                        for (int i = 0; i < moviesArray.length(); i++) {
                            JSONObject movieObject = moviesArray.getJSONObject(i);

                            // Extract basic details from each movie
                            String title = movieObject.getString("Title");
                            String year = "Year: " + movieObject.getString("Year");
                            String imdbID = movieObject.getString("imdbID");

                            // Fetch additional details for each movie by IMDb ID
                            fetchMovieDetails(imdbID, title, year, movieList);
                        }

                    } catch (JSONException e) {
                        // Log an error if JSON parsing fails
                        Log.e("API_ERROR", "JSON parsing error: " + e.getMessage());
                    }
                } else {
                    // Log an error if the API response is not successful
                    Log.e("API_ERROR", "Request failed: " + response.message());
                }
            }
        });
    }

    // Fetches detailed movie information by IMDb ID
    private void fetchMovieDetails(String imdbID, String title, String year, List<Movie> movieList) {
        // Build the URL for fetching detailed movie info
        String url = "https://www.omdbapi.com/?i=" + imdbID + "&apikey=c6d84029";

        // Create an OkHttpClient for making network requests
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Make a network call to fetch detailed movie information
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Log an error if the network request fails
                Log.e("API_ERROR", "Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // Parse the JSON response from the server
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        // Extract additional movie details
                        String studio = "Studio: " + jsonObject.optString("Production", "N/A");
                        String rating = "Rating: " + jsonObject.optString("imdbRating", "N/A");

                        // Create a Movie object with detailed data
                        Movie movie = new Movie(title, studio, rating, year, imdbID);

                        // Add the movie to the list
                        movieList.add(movie);

                        // Update the LiveData on the main thread
                        movies.postValue(movieList);

                    } catch (JSONException e) {
                        // Log an error if JSON parsing fails
                        Log.e("API_ERROR", "JSON parsing error: " + e.getMessage());
                    }
                }
            }
        });
    }
}
