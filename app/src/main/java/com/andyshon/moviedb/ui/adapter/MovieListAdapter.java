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
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.ui.MovieClickCallback;
import com.andyshon.moviedb.ui.activity.MovieListActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by andyshon on 06.08.18.
 */

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    @Nullable
    private final MovieClickCallback mMovieClickCallback;

    private String image_proper_size;

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<MovieResult> movieResults;

    private boolean isLoadingAdded = false;

    public MovieListAdapter(@Nullable MovieClickCallback clickCallback) {
        this.mMovieClickCallback = clickCallback;
        Context context = (MovieListActivity) mMovieClickCallback;
        movieResults = new ArrayList<>();

        image_proper_size = GlobalConstants.getImageSize(context, false).getSize();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_movie, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {

        MovieResult movie = movieResults.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH holder = (MovieVH) h;

                if (movie.getPoster_path() != null) {
                    String imagePath = GlobalConstants.ApiConstants.IMAGE_PATH.concat(image_proper_size).concat("/").concat(movie.getPoster_path());
                    Picasso.get().load(imagePath).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_foreground).into(holder.image);
                }

                holder.card.setOnClickListener(view -> {
                    mMovieClickCallback.onClick(movie);
                    GlobalConstants.ApiConstants.CURRENT_MOVIE_ID = movie.getId();
                });

                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }



    public void add(MovieResult result) {
        movieResults.add(result);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<MovieResult> moveResults) {
        for (MovieResult result : moveResults) {
            add(result);
        }
    }

    public void remove(MovieResult result) {
        int position = movieResults.indexOf(result);
        if (position > -1) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new MovieResult());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        MovieResult result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public MovieResult getItem(int position) {
        return movieResults.get(position);
    }



    protected class MovieVH extends RecyclerView.ViewHolder {
        final ImageView image;
        final CardView card;

        MovieVH(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            card = view.findViewById(R.id.movie_item);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {
        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}
