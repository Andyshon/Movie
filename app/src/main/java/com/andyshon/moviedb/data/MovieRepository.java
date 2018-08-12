package com.andyshon.moviedb.data;

import android.content.Context;

import com.andyshon.moviedb.R;
import com.andyshon.moviedb.data.entity.Movie;
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.data.entity.MovieSearch;
import com.andyshon.moviedb.data.entity.MovieSearchResult;
import com.andyshon.moviedb.data.entity.MovieTrailer;
import com.andyshon.moviedb.data.local.MoviesDao;
import com.andyshon.moviedb.data.remote.TheMovieDbService;

import javax.inject.Inject;

import io.reactivex.Observable;

import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.*;

/**
 * Created by andyshon on 06.08.18.
 */

public class MovieRepository {

    private MoviesDao moviesDao;
    private TheMovieDbService theMovieDbService;

    private Context context;

    @Inject
    public MovieRepository (MoviesDao moviesDao, TheMovieDbService theMovieDbService, Context context) {
        this.moviesDao = moviesDao;
        this.theMovieDbService = theMovieDbService;
        this.context = context;
    }


    /**
     * Returns popular movies
     */
    public Observable<Movie> getPopularMovies () {

        Observable<Movie> observableFromApi = null, observableFromDb = null;
        if (Utils.hasInternet(context))
            observableFromApi = getPopularMoviesFromApi();
        observableFromDb = getPopularMoviesFromDb();

        if (Utils.hasInternet(context))
            return Observable.concatArrayEager(observableFromApi, observableFromDb);
        else return observableFromDb;
    }

    /**
     * Returns popular movies from server API
     */
    private Observable<Movie> getPopularMoviesFromApi() {
        return theMovieDbService.getPopularMovies(API_KEY, String.valueOf(R.string.language), CURRENT_PAGE)
                .doOnNext(movie -> {
                    for (MovieResult movieResult : movie.getMovies())
                        moviesDao.insertSingleMovie(movieResult);
                    moviesDao.insertMoviePopular(movie);
                });
    }

    /**
     * Returns popular movies from Room
     */
    private Observable<Movie> getPopularMoviesFromDb() {
        return moviesDao.queryPopularMovies(CURRENT_PAGE).toObservable();
    }


    /**
     * Returns movie by id, also store movie in Room
     */
    public Observable<MovieResult> getMovieById () {

        Observable<MovieResult> observableFromApi = null, observableFromDb = null;
        if (Utils.hasInternet(context))
            observableFromApi = getMovieByIdFromApi();
        observableFromDb = getMovieByIdFromDb();

        if (Utils.hasInternet(context))
            return Observable.concatArrayEager(observableFromApi, observableFromDb);
        else return observableFromDb;
    }

    /**
     * Returns movie by id from server API
     */
    private Observable<MovieResult> getMovieByIdFromApi() {
        return theMovieDbService.getMovieById(CURRENT_MOVIE_ID, API_KEY, String.valueOf(R.string.language));
    }

    /**
     * Returns movie by id from Room
     */
    private Observable<MovieResult> getMovieByIdFromDb() {
        return moviesDao.queryMovieById(CURRENT_MOVIE_ID).toObservable();
    }


    /**
     * Returns trailer by movie id
     */
    public Observable<MovieTrailer> getTrailerByMovieId () {
        if (Utils.hasInternet(context))
            return getTrailerByMovieIdFromApi();
        else return getTrailerByMovieIdFromApi();
    }

    /**
     * Returns trailer by movie id from server API
     */
    private Observable<MovieTrailer> getTrailerByMovieIdFromApi() {
        return theMovieDbService.getTrailersByMovieId(CURRENT_MOVIE_ID, API_KEY, String.valueOf(R.string.language));
    }



    /**
     * Returns search movie
     * @param query String movie title
     */
    public Observable<MovieSearch> getSearchMovies (String query) {

        Observable<MovieSearch> observableFromApi = null, observableFromDb = null;
        if (Utils.hasInternet(context))
            observableFromApi = getSearchMoviesFromApi(query);
        observableFromDb = getSearchMoviesFromDb(query);

        if (Utils.hasInternet(context))
            return Observable.concatArrayEager(observableFromApi, observableFromDb);
        else return observableFromDb;
    }

    /**
     * Returns search movie from server API, also insert found search movie to Room
     * @param query String movie title
     */
    private Observable<MovieSearch> getSearchMoviesFromApi(String query) {
        return theMovieDbService.getSearchMovies(API_KEY, String.valueOf(R.string.language), query, CURRENT_PAGE, false)
                .doOnNext(movie -> {
                    for (MovieSearchResult movieResult : movie.getMovies()) {
                        moviesDao.insertSingleMovieSearch(movieResult);
                    }
                    moviesDao.insertMovieSearch(movie);
                });
    }

    /**
     * Returns search movie from Room
     * @param query String movie title. Not used. Find movies in first page.
     */
    private Observable<MovieSearch> getSearchMoviesFromDb(String query) {
        return moviesDao.queryMovieSearch(CURRENT_PAGE).toObservable();
    }


    /**
     * Returns search movie by id
     */
    public Observable<MovieSearchResult> getMovieSearchById () {

        Observable<MovieSearchResult> observableFromApi = null, observableFromDb = null;
        if (Utils.hasInternet(context))
            observableFromApi = getMovieSearchByIdFromApi();
        observableFromDb = getMovieSearchByIdFromDb();

        if (Utils.hasInternet(context))
            return Observable.concatArrayEager(observableFromApi, observableFromDb);
        else return observableFromDb;
    }

    /**
     * Returns search movie by id from server API, also insert found movie to Room
     */
    private Observable<MovieSearchResult> getMovieSearchByIdFromApi() {
        return theMovieDbService.getMovieSearchById(CURRENT_MOVIE_ID, API_KEY, String.valueOf(R.string.language));
    }

    /**
     * Returns search movie by id from Room
     */
    private Observable<MovieSearchResult> getMovieSearchByIdFromDb() {
        return moviesDao.queryMovieSearchById(CURRENT_MOVIE_ID).toObservable();
    }
}
