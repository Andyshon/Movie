package com.andyshon.moviedb.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andyshon.moviedb.R;
import com.andyshon.moviedb.GlobalConstants;
import com.andyshon.moviedb.data.entity.MovieSearchResult;
import com.andyshon.moviedb.ui.MovieSearchClickCallback;
import com.andyshon.moviedb.ui.activity.MovieSearchActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andyshon on 06.08.18.
 */

public class MovieSearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    @Nullable
    private final MovieSearchClickCallback mMovieClickCallback;

    private List<MovieSearchResult> movies;

    private String image_proper_size;
//    private static final int ITEM = 0;

//    private static final int LOADING = 1;

//    private boolean isLoadingAdded = false;


    public MovieSearchListAdapter(@Nullable MovieSearchClickCallback clickCallback) {
        this.mMovieClickCallback = clickCallback;
        Context context = (MovieSearchActivity) mMovieClickCallback;
        movies = new ArrayList<>();

        image_proper_size = GlobalConstants.getImageSize(context, false).getSize();
    }


    /*public void setMoviesList(final List<MovieSearchResult> moviesList) {
        movies = moviesList;
        notifyDataSetChanged();
    }*/


    public void add(MovieSearchResult result) {
        movies.add(result);
        notifyItemInserted(movies.size() - 1);
    }


    public void addAll(List<MovieSearchResult> moveResults) {
        for (MovieSearchResult result : moveResults) {
            add(result);
        }
    }

    public void remove(MovieSearchResult result) {
        int position = movies.indexOf(result);
        if (position > -1) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        //isLoadingAdded = false;
        for (MovieSearchResult search : movies) {
            remove(search);
        }
        /*while (getItemCount() > 0) {
            remove(movies.);
        }*/
    }

    public void setMoviesList(final List<MovieSearchResult> moviesList) {
        movies = moviesList;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        MovieSearchResult currMovie = movies.get(position);

        MovieViewHolder holder = (MovieViewHolder) h;

        if (currMovie.getPoster_path() != null) {
            String imagePath = GlobalConstants.ApiConstants.IMAGE_PATH.concat(image_proper_size).concat("/").concat(currMovie.getPoster_path());
            Picasso.get().load(imagePath).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_foreground).into(holder.image);
        }

        holder.card.setOnClickListener(view -> {
            mMovieClickCallback.onClick(currMovie);
            GlobalConstants.ApiConstants.CURRENT_MOVIE_ID = currMovie.getId();
        });
    }


    /*@Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieSearchResult currMovie = movies.get(position);

        if (currMovie.getPoster_path() != null) {
            String imagePath = GlobalConstants.ApiConstants.IMAGE_PATH.concat(image_proper_size).concat("/").concat(currMovie.getPoster_path());
            Picasso.get().load(imagePath).into(holder.image);
        }

        holder.card.setOnClickListener(view -> {
            mMovieClickCallback.onClick(currMovie);
            GlobalConstants.ApiConstants.CURRENT_MOVIE_ID = currMovie.getId();
        });
    }*/


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
