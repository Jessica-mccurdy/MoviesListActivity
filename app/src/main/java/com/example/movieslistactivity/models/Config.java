package com.example.movieslistactivity.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    // the base url for loading images
    String imageBaseUrl;
    // poster size used when fetching images - part of url
    String posterSize;



    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        System.out.println("in the try guess it worked");
        // get image base url
        imageBaseUrl = images.getString("secure_base_url");
        // get poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //use the option at index 3 or w342 as a fallback
        posterSize = posterSizeOptions.optString(3, "w342");
    }

    // helper method for creating urls
    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path); // concat all three
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
