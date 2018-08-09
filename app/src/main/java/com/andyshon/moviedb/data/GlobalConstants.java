package com.andyshon.moviedb.data;

import java.util.HashMap;

/**
 * Created by andyshon on 06.08.18.
 */

public class GlobalConstants {
    public static class ApiConstants {
        public static final String BASE_URL = "https://api.themoviedb.org/3/";
        public static final String API_KEY = "d8405b2ea6a47d85be8ea1d8baae6a92";
        public static final String LANGUAGE = "ru-RU";
        public static final String IMAGE_PATH_W500 = "https://image.tmdb.org/t/p/w500/";

        public static int CURRENT_MOVIE_ID;

        public static HashMap<String, Float> TRAILER_START_SECONDS = new HashMap<>();

        public static int CURRENT_PAGE = 1;
    }
}
