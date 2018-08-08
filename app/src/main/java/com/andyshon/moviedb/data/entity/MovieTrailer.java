package com.andyshon.moviedb.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andyshon on 07.08.18.
 */

public class MovieTrailer {
    public MovieTrailer() {}

    @SerializedName("results")
    private List<MovieTrailerResult> trailers;

    public List<MovieTrailerResult> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<MovieTrailerResult> trailers) {
        this.trailers = trailers;
    }
}
