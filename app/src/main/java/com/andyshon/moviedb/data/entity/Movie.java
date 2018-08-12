package com.andyshon.moviedb.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.andyshon.moviedb.data.entity.converters.ListTypeConverters_movie;
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

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int total_pages;

    @SerializedName("total_results")
    private int total_results;

    @TypeConverters(ListTypeConverters_movie.class)
    @SerializedName("results")
    private List<MovieResult> movies;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public List<MovieResult> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieResult> movies) {
        this.movies = movies;
    }
}
