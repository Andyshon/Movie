package com.andyshon.moviedb.data;

import android.util.Log;

import com.andyshon.moviedb.data.entity.Movie;
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.data.entity.MovieTrailer;
import com.andyshon.moviedb.data.remote.RestClient;

import io.reactivex.Observable;

import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.API_KEY;
import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.CURRENT_MOVIE_ID;
import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.CURRENT_PAGE;
import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.LANGUAGE;

/**
 * Created by andyshon on 06.08.18.
 */

public class MovieRepository {

    private static final String TAG = "MovieRepository";

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
        Log.d(TAG, "hasConnection:" + hasConnection);

        Observable<Movie> observableFromApi = null, observableFromDb = null;
        if (hasConnection) {
            observableFromApi = getPopularMoviesFromApi();
        }
        observableFromDb = getPopularMoviesFromDb();

        if (hasConnection) {
            return Observable.concatArrayEager(observableFromApi, observableFromDb);
        }
        else
            return observableFromDb;
    }

    private Observable<Movie> getPopularMoviesFromApi() {
        return RestClient.getService().getPopularMovies(API_KEY, LANGUAGE, CURRENT_PAGE)
                .doOnNext(movie -> {
                    // insert every movie to local db
                    for (MovieResult movieResult : movie.getMovies()) {
                        mDatabase.moviesDao().insertSingleMovie(movieResult);
                    }
                    mDatabase.moviesDao().insertMoviePopular(movie);
                });
    }

    private Observable<Movie> getPopularMoviesFromDb() {
        return mDatabase.moviesDao().queryPopularMovies(CURRENT_PAGE)
                .toObservable()
                .doOnNext(movie -> {
                    Log.e(TAG, "queryPopularMovies from db:" + String.valueOf(movie.getMovies().size()));
                });
    }


    public Observable<MovieResult> getMovieById (boolean hasConnection) {
        Log.d(TAG, "hasConnection:" + hasConnection);

        Observable<MovieResult> observableFromApi = null, observableFromDb = null;
        if (hasConnection) {
            observableFromApi = getMovieByIdFromApi();
        }
        observableFromDb = getMovieByIdFromDb();

        if (hasConnection) {
            return Observable.concatArrayEager(observableFromApi, observableFromDb);
        }
        else
            return observableFromDb;
    }

    private Observable<MovieResult> getMovieByIdFromApi() {
        return RestClient.getService().getMovieById(CURRENT_MOVIE_ID, API_KEY, LANGUAGE)
                .doOnError(throwable -> System.out.println("THROWABLE:" + throwable.getMessage()))
                .doOnNext(movie -> {
                    // insert single movie to local db
                    mDatabase.moviesDao().insertSingleMovie(movie);
                });
    }

    private Observable<MovieResult> getMovieByIdFromDb() {
        return mDatabase.moviesDao().queryMovieById(CURRENT_MOVIE_ID)
                .toObservable()
                .doOnNext(movieResult -> Log.e(TAG, "Movie from db:" + movieResult.getTitle()));
    }



    public Observable<MovieTrailer> getTrailerByMovieId (boolean hasConnection) {
        Log.d(TAG, "hasConnection:" + hasConnection);
        if (hasConnection) {
            return getTrailerByMovieIdFromApi();
        }
        else {
            return getTrailerByMovieIdFromApi();
        }
    }

    private Observable<MovieTrailer> getTrailerByMovieIdFromApi() {
        return RestClient.getService().getTrailersByMovieId(CURRENT_MOVIE_ID, API_KEY, LANGUAGE)
                .doOnNext(movieResult -> {
                    Log.d(TAG, "Get movie trailer by id:" + movieResult.getTrailers().size());
                });
    }

}
