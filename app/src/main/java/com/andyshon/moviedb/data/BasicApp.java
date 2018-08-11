package com.andyshon.moviedb.data;

import android.app.Application;

import com.andyshon.moviedb.data.di.ActivityComponent;
import com.andyshon.moviedb.data.di.ActivityModule;
import com.andyshon.moviedb.data.di.DaggerActivityComponent;
import com.andyshon.moviedb.data.di.RoomModule;

/**
 * Created by andyshon on 06.08.18.
 */

public class BasicApp extends Application {


    private static BasicApp app;

    ActivityComponent activityComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        initDataComponent();

        activityComponent.inject(this);
    }

    private void initDataComponent() {
        activityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .roomModule(new RoomModule(this))
                .build();
    }

    public static BasicApp getApp() {
        return app;
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
