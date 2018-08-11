package com.andyshon.moviedb.data.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.andyshon.moviedb.data.AppDatabase;
import com.andyshon.moviedb.data.local.MoviesDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by andyshon on 11.08.18.
 */

@Module
public class RoomModule {

    private AppDatabase database;

    public RoomModule(Application mApplication) {
        database = Room.databaseBuilder(mApplication, AppDatabase.class, "movie-db").build();
    }

    @Singleton
    @Provides
    AppDatabase providesRoomDatabase() {
        return database;
    }

    @Singleton
    @Provides
    MoviesDao providesMoviesDao(AppDatabase appDatabase) {
        return appDatabase.moviesDao();
    }
}
