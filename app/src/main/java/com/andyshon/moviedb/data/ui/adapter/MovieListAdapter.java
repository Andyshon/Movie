package com.andyshon.moviedb.data.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andyshon.moviedb.R;
import com.andyshon.moviedb.data.GlobalConstants;
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.data.ui.MovieClickCallback;
import com.andyshon.moviedb.data.ui.activity.MovieListActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by andyshon on 06.08.18.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>{
    private List<MovieResult> movies;
    private String image_proper_size;

    @Nullable
    private final MovieClickCallback mMovieClickCallback;


    public MovieListAdapter(@Nullable MovieClickCallback clickCallback) {
        this.mMovieClickCallback = clickCallback;
        Context context = (MovieListActivity) mMovieClickCallback;

        image_proper_size = GlobalConstants.getImageSize(context, false).getSize();
    }


    public void setMoviesList(final List<MovieResult> moviesList) {
        movies = moviesList;
        notifyDataSetChanged();
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieResult currMovie = movies.get(position);

        if (currMovie.getPoster_path() != null) {
            String imagePath = GlobalConstants.ApiConstants.IMAGE_PATH.concat(image_proper_size).concat("/").concat(currMovie.getPoster_path());
            Picasso.get().load(imagePath).into(holder.image);
        }

        holder.card.setOnClickListener(view -> {
            mMovieClickCallback.onClick(currMovie);
            GlobalConstants.ApiConstants.CURRENT_MOVIE_ID = currMovie.getId();
        });
    }


    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }


    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final ImageView image;
        final CardView card;

        MovieViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            card = view.findViewById(R.id.movie_item);
        }
    }
}
