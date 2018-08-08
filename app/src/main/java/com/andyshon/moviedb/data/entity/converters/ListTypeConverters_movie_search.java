package com.andyshon.moviedb.data.entity.converters;

import android.arch.persistence.room.TypeConverter;

import com.andyshon.moviedb.data.entity.MovieSearchResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by andyshon on 06.08.18.
 */

public class ListTypeConverters_movie_search {
    static Gson gson = new Gson();

    @TypeConverter
    public static List<MovieSearchResult> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<MovieSearchResult>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String objectsToString(List<MovieSearchResult> someObjects) {
        return gson.toJson(someObjects);
    }
}
