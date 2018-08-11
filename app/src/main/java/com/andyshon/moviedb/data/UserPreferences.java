package com.andyshon.moviedb.data;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by andyshon on 10.08.18.
 */

public class UserPreferences {

    private static final String _SP_LAST_SEARCH_QUERY = "LSQ";

    public static void saveLastSearchQuery(Activity activity, String s) {
        SharedPreferences sPref = activity.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(_SP_LAST_SEARCH_QUERY, s);
        ed.apply();
    }

    public static String loadLastSearchQuery(Activity activity) {
        SharedPreferences sPref = activity.getPreferences(MODE_PRIVATE);
        return sPref.getString(_SP_LAST_SEARCH_QUERY, "");
    }
}
