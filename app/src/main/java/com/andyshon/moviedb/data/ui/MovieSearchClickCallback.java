package com.andyshon.moviedb.data.ui;

import com.andyshon.moviedb.data.entity.MovieSearchResult;

/**
 * Created by andyshon on 06.08.18.
 */

public interface MovieSearchClickCallback {
    void onClick(MovieSearchResult movie);
}
