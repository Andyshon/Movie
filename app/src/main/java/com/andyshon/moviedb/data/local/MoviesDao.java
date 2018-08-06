package com.andyshon.moviedb.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import com.andyshon.moviedb.data.entity.Movie;

/**
 * Created by andyshon on 06.08.18.
 */

@Dao
public interface MoviesDao {

    @Insert
    void insertMovie(Movie movie);

}
