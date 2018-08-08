package com.andyshon.moviedb.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.andyshon.moviedb.data.entity.Movie;
import com.andyshon.moviedb.data.entity.MovieResult;

import io.reactivex.Single;

/**
 * Created by andyshon on 06.08.18.
 */

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM Movie")
    Single<Movie> queryPopularMovies();

    @Query("SELECT * FROM MovieResult where id = :id")
    Single<MovieResult> queryMovieById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMoviePopular(MovieResult movieResult);

}
