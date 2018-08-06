package com.andyshon.moviedb.data.remote;

import com.andyshon.moviedb.data.entity.Movie;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by andyshon on 06.08.18.
 */

public interface IService {

    @GET("popular")
    Observable<Movie> fetchPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

}
