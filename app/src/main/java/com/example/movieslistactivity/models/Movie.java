package com.example.movieslistactivity.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel // annotation indicates class is Parcelable
public class Movie {

    // values from API
    String title;
    String overview;
    String posterPath; //not full url
    String backdropPath;
    Double voteAverage;

    // no-arg, empty constructor required for Parceler
    public Movie() {}

    // initialize from json


    // initialize from JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");


    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
