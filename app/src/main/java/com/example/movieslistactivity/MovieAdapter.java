package com.example.movieslistactivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieslistactivity.models.Config;
import com.example.movieslistactivity.models.Movie;

import java.util.ArrayList;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // list of movies
    ArrayList<Movie> movies;
    Config config;
    Context context;

    // initialize with list

    public MovieAdapter(ArrayList<Movie> movies) {

        this.movies = movies;
    }

    public Config getConfig(){
        return config;
    }

    public void setConfig(Config config) {

        this.config = config;
    }

    // creates and inflates new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie,parent, false);
        return new ViewHolder(movieView);
    }

    // binds inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the movie data at the specified position
        Movie movie = movies.get(position);
        // populate the view with movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        // build url for poster image
        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());

        // load images using glide
        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(20))
                        .placeholder(R.drawable.flicks_movie_placeholder)
                         .error(R.drawable.flicks_movie_placeholder))
                .into(holder.ivPosterImage);
    }


    // returns the total number of items in list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create the viewholder as a static linear class
    public static class ViewHolder extends RecyclerView.ViewHolder {


        // track view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
