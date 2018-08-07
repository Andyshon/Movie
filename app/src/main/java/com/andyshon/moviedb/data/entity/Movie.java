package com.andyshon.moviedb.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andyshon on 06.08.18.
 */

@Entity(tableName = "Movie")
public class Movie {

    public Movie() {}

    @PrimaryKey(autoGenerate = true)
    private int id;

    @TypeConverters(ListTypeConverters_movie.class)
    @SerializedName("results")
    private List<MovieResult> movies;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieResult> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieResult> movies) {
        this.movies = movies;
    }
}
