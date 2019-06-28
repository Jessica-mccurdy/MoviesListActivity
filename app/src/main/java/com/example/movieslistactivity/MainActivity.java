package com.example.movieslistactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.movieslistactivity.models.Config;
import com.example.movieslistactivity.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {


    //constants
    //the base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // parameter name for API key
    public final static String API_KEY_PARAM = "api_key";
    // API key ---TODO move to a secure location
    // tag for logging from activity
    public final static String TAG = "MainActivity";

    // instance fields
    AsyncHttpClient client;
    // the list of current movies
    ArrayList<Movie> movies;
    // the recycler view
    RecyclerView rvMovies;
    // the adapter wired to the recycler view
    MovieAdapter adapter;
    // image config
    Config config;  // why is this red?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize the client
        client = new AsyncHttpClient();
        // initialize list of movies
        movies = new ArrayList<>();
        // initialize the adapter -- movies array cannot be reinitialized after this point
        adapter = new MovieAdapter(movies);

        // resolve the recycle ciew and connect a layout manager and adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        // get the configuration on app creation
        getConfiguration();


    }

    // get the list of current movies
    private void getNowPlaying(){
        // create the url
        String url = API_BASE_URL + "/movie/now_playing";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key, always required
        System.out.println("about to try");
        // execute a Get request expecting JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into movie list
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate through result to create movie objects
                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        // notify adapter that a row was added
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("loaded %s movies", results.length()));

                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now_playing endpoint", throwable, true );
            }
        });
    }

    // get configuration from the API
    private void getConfiguration() {
        System.out.println("in getConfig");
        // create the url
        String url = API_BASE_URL + "/configuration";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key, always required
        System.out.println("about to try");
        // execute a Get request expecting JSON object response
        client.get(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    config = new Config(response);

                    Log.i(TAG,
                            String.format("Loaded configuration with imageBaseUrl %s and posterSize %s",
                                    config.getImageBaseUrl(),
                                    config.getPosterSize()));
                    //pass config to adapter
                    adapter.setConfig(config);
                    getNowPlaying();
                } catch (JSONException e) {

                    System.out.println("caught");
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,String responseString, Throwable throwable) {
                System.out.println("onFaliure");
                logError("Failed getting configuration", throwable, true);
            }
        });
    }

    //handle errors, log, and alert user
    private void  logError(String message, Throwable error, boolean alertUser) {
        // always log the error
        Log.e(TAG, message, error);
        // alert user- avoid silent errors
        if (alertUser){
            //show a long toast with error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
