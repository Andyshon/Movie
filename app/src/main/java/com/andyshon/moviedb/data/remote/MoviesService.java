package com.andyshon.moviedb.data.remote;

import com.andyshon.moviedb.data.entity.Movie;
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.data.entity.MovieSearch;
import com.andyshon.moviedb.data.entity.MovieSearchResult;
import com.andyshon.moviedb.data.entity.MovieTrailer;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by andyshon on 06.08.18.
 */

public interface MoviesService {

    @GET("movie/popular")
    Observable<Movie> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{id}")
    Observable<MovieResult> getMovieById(
            @Path("id") int movieID,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{id}")
    Observable<MovieSearchResult> getMovieSearchById(
            @Path("id") int movieID,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{id}/videos")
    Observable<MovieTrailer> getTrailersByMovieId(
            @Path("id") int movieID,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("search/movie")
    Observable<MovieSearch> getSearchMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page,
            @Query("include_adult") boolean adult
    );

}
