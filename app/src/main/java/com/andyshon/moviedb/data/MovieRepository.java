package com.andyshon.moviedb.data;

import com.andyshon.moviedb.data.entity.Movie;
import com.andyshon.moviedb.data.remote.RestClient;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.API_KEY;
import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.LANGUAGE;

/**
 * Created by andyshon on 06.08.18.
 */

public class MovieRepository {

    private static MovieRepository sInstance;

    private final AppDatabase mDatabase;


    private MovieRepository(final AppDatabase database) {
        mDatabase = database;
    }

    public static MovieRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (MovieRepository.class) {
                if (sInstance == null) {
                    sInstance = new MovieRepository(database);
                }
            }
        }
        return sInstance;
    }

    public Observable<Movie> fetchPopularMovies () {
        // assume that we have internet connection
        Observable<Movie> observableFromApi = null;
        observableFromApi = get();

        return observableFromApi;
    }

    private Observable<Movie> get() {
        return RestClient.getService().fetchPopularMovies(API_KEY, LANGUAGE, 1)
                .doOnNext(movie -> {
                    // insert every movie to local db
                    //mDatabase.moviesDao().insertMovie(movie);
                });
    }

}
