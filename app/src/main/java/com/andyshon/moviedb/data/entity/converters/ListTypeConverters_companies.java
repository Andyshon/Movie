package com.andyshon.moviedb.data.entity.converters;

import android.arch.persistence.room.TypeConverter;

import com.andyshon.moviedb.data.entity.MovieSearchResult_productionCompanies;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by andyshon on 06.08.18.
 */

public class ListTypeConverters_companies {
    static Gson gson = new Gson();

    @TypeConverter
    public static List<MovieSearchResult_productionCompanies> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<MovieSearchResult_productionCompanies>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String objectsToString(List<MovieSearchResult_productionCompanies> someObjects) {
        return gson.toJson(someObjects);
    }
}
