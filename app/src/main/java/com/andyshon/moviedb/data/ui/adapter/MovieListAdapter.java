package com.andyshon.moviedb.data.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andyshon.moviedb.R;
import com.andyshon.moviedb.data.GlobalConstants;
import com.andyshon.moviedb.data.entity.MovieResults;
import com.andyshon.moviedb.data.ui.MovieClickCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by andyshon on 06.08.18.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>{
    private List<MovieResults> movies;

    @Nullable
    private final MovieClickCallback mMovieClickCallback;


    public MovieListAdapter(@Nullable MovieClickCallback clickCallback) {
        mMovieClickCallback = clickCallback;
    }


    public void setMoviesList(final List<MovieResults> moviesList) {
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
        MovieResults currMovie = movies.get(position);

        holder.title.setText(currMovie.getTitle());

        String imagePath = GlobalConstants.ApiConstants.IMAGE_PATH_W500.concat(currMovie.getPoster_path());
        Picasso.get().load(imagePath).into(holder.image);

        holder.card.setOnClickListener(view -> mMovieClickCallback.onClick(currMovie));
    }


    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }


    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final ImageView image;
        final AppCompatTextView title;
        final CardView card;

        MovieViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            card = view.findViewById(R.id.movie_item);
        }
    }
}
