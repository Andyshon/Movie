package com.andyshon.moviedb.data;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import java.util.HashMap;

import static android.content.res.Configuration.*;

/**
 * Created by andyshon on 06.08.18.
 */

public class GlobalConstants {

    public static class ApiConstants {
        public static final String BASE_URL = "https://api.themoviedb.org/3/";
        public static final String API_KEY = "d8405b2ea6a47d85be8ea1d8baae6a92";
        public static final String IMAGE_PATH = "https://image.tmdb.org/t/p/";

        public static HashMap<String, Float> TRAILER_START_SECONDS = new HashMap<>();
        public static int CURRENT_MOVIE_ID;
        public static int CURRENT_PAGE = 1;
    }


    public enum ImagesConfiguration {
        SMALL("Small", "w500"),
        NORMAL("Normal", "w780"),
        LARGE("Large", "w1280"),
        XLARGE("XLarge", "original");

        private String stringValue;
        private String intValue;

        ImagesConfiguration(String s, String value) {
            stringValue = s;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public String getSize(){
            return intValue;
        }
    }


    public static ImagesConfiguration getImageSize (Context context, boolean isPoster) {

        ImagesConfiguration imagesSizes = null;
        Configuration config = context.getResources().getConfiguration();

        if((config.screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_SMALL) {
            imagesSizes = ImagesConfiguration.SMALL;
        }
        if((config.screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_NORMAL) {
            imagesSizes = ImagesConfiguration.NORMAL;
        }
        if((config.screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) {
            if (isPoster) {
                imagesSizes = ImagesConfiguration.NORMAL;
            }
            else
                imagesSizes = ImagesConfiguration.LARGE;
        }
        if((config.screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE) {
            imagesSizes = ImagesConfiguration.XLARGE;
        }

        Log.e("GlobalConstants:", "ImagesConfiguration:" + imagesSizes.getStringValue());

        return imagesSizes;
    }
}
