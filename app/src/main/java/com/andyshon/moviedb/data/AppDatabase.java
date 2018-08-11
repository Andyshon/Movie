package com.andyshon.moviedb.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.VisibleForTesting;

import com.andyshon.moviedb.data.entity.Movie;
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.data.entity.MovieSearch;
import com.andyshon.moviedb.data.entity.MovieSearchResult;
import com.andyshon.moviedb.data.local.MoviesDao;

/**
 * Created by andyshon on 06.08.18.
 */

@Database(entities = {Movie.class, MovieResult.class, MovieSearch.class, MovieSearchResult.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    @VisibleForTesting
    public static final String DATABASE_NAME = "movie-db";

    public abstract MoviesDao moviesDao();
}
