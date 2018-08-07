package com.andyshon.moviedb.data.remote;

import com.andyshon.moviedb.data.entity.Movie;
import com.andyshon.moviedb.data.entity.MovieResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by andyshon on 06.08.18.
 */

public interface IService {

    @GET("movie/popular")
    Observable<Movie> fetchPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{id}")
    Observable<MovieResult> fetchMovieById(
            @Path("id") int movieID,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{id}/videos")
    Observable<MovieResult> fetchTrailersByMovieId(
            @Path("id") int movieID,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

}
