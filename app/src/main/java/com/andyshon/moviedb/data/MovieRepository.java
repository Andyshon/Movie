package com.andyshon.moviedb.data;

import com.andyshon.moviedb.data.entity.Movie;
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.data.remote.RestClient;

import io.reactivex.Observable;

import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.API_KEY;
import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.CURRENT_MOVIE_ID;
import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.LANGUAGE;

/**
 * Created by andyshon on 06.08.18.
 */

public class MovieRepository {

    private static MovieRepository sInstance;

    private AppDatabase mDatabase;

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

    public Observable<Movie> getPopularMovies (boolean hasConnection) {
        // check internet connection
        System.out.println("hasConnection:" + hasConnection);
        if (hasConnection) {
            return getPopularMoviesFromApi();
        }
        else {
            return getPopularMoviesFromApi();
        }
    }

    private Observable<Movie> getPopularMoviesFromApi() {
        return RestClient.getService().fetchPopularMovies(API_KEY, LANGUAGE, 1)
                .doOnNext(movie -> {
                    // insert every movie to local db
                    //mDatabase.moviesDao().insertMovie(movie);
                });
    }



    public Observable<MovieResult> getPopularMovieById (boolean hasConnection) {
        // check internet connection
        System.out.println("hasConnection:" + hasConnection);
        if (hasConnection) {
            return getMovieByIdFromApi();
        }
        else {
            return getMovieByIdFromApi();
        }
    }

    private Observable<MovieResult> getMovieByIdFromApi() {
        return RestClient.getService().fetchMovieById(CURRENT_MOVIE_ID, API_KEY, LANGUAGE)
                .doOnNext(movie -> {
                    System.out.println("GET MOVIE BY ID:" + movie.getTitle()+":"+movie.getBackdrop_path());
                    // insert every movie to local db
                    //mDatabase.moviesDao().insertMovie(movie);
                });
    }

}
