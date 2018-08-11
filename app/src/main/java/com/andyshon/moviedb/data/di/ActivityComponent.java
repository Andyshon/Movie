package com.andyshon.moviedb.data.di;

import com.andyshon.moviedb.data.BasicApp;
import com.andyshon.moviedb.data.MovieRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by andyshon on 11.08.18.
 */

@Component(modules = {ActivityModule.class, RoomModule.class})
@Singleton
public interface ActivityComponent {

    void inject(BasicApp basicApp);

    MovieRepository movieRepository();
}
