package com.andyshon.moviedb.data;

import android.app.Application;

/**
 * Created by andyshon on 06.08.18.
 */

public class BasicApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public MovieRepository getRepository() {
        return MovieRepository.getInstance(getDatabase());
    }
}
