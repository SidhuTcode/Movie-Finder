package com.example.moviefinder.model;
public class Movie {
    // Fields for movie information
    String title;
    String studio;
    String rating;
    String year;
    String imdbID;
    String posterUrl;
    String description;


    public Movie(String title, String studio, String rating, String year, String imdbID,String description,String posterUrl) {
        this.title = title;
        this.studio = studio;
        this.rating = rating;
        this.year = year;
        this.imdbID = imdbID;
        this.description = description;
        this.posterUrl = posterUrl;
    }

    public Movie(String title, String studio, String rating, String year, String imdbID) {
        this.title = title;
        this.studio = studio;
        this.rating = rating;
        this.year = year;
        this.imdbID = imdbID;
    }


    // Getter methods to access movie details
    public String getTitle() {
        return title;
    }

    public String getStudio() {
        return studio;
    }

    public String getRating() {
        return rating;
    }

    public String getYear() {
        return year;
    }

    public String getImdbId(){
        return imdbID;
    }
    public String getDescription(){
        return description;
    }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}

