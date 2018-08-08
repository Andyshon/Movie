package com.andyshon.moviedb.data.ui.adapter;

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
import com.andyshon.moviedb.data.entity.MovieSearch;
import com.andyshon.moviedb.data.entity.MovieSearchResult;
import com.andyshon.moviedb.data.ui.MovieClickCallback;
import com.andyshon.moviedb.data.ui.MovieSearchClickCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by andyshon on 06.08.18.
 */

public class MovieSearchListAdapter extends RecyclerView.Adapter<MovieSearchListAdapter.MovieViewHolder>{
    private MovieSearch movies;

    @Nullable
    private final MovieSearchClickCallback mMovieClickCallback;


    public MovieSearchListAdapter(@Nullable MovieSearchClickCallback clickCallback) {
        mMovieClickCallback = clickCallback;
    }


    public void setMoviesList(final MovieSearch moviesList) {
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
        MovieSearchResult currMovie = movies.getMovies().get(position);

        if (currMovie.getPoster_path() != null) {
            String imagePath = GlobalConstants.ApiConstants.IMAGE_PATH_W500.concat(currMovie.getPoster_path());
            Picasso.get().load(imagePath).into(holder.image);
        }

        System.out.println("ID:" + currMovie.getId());

        holder.card.setOnClickListener(view -> {
            System.out.println("CLICK ID:" + currMovie.getId());
            mMovieClickCallback.onClick(currMovie);
            GlobalConstants.ApiConstants.CURRENT_MOVIE_ID = currMovie.getId();
        });
    }


    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.getMovies().size();
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
