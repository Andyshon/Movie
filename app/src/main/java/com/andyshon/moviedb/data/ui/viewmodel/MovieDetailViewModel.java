package com.andyshon.moviedb.data.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.andyshon.moviedb.data.BasicApp;
import com.andyshon.moviedb.data.MovieRepository;
import com.andyshon.moviedb.data.Utils;
import com.andyshon.moviedb.data.entity.MovieResult;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by andyshon on 07.08.18.
 */

public class MovieDetailViewModel extends AndroidViewModel {

    private MutableLiveData<MovieResult> movieResult;
    private MutableLiveData<String> movieError;
    private MutableLiveData<Boolean> movieLoader;
    private MovieRepository repository;
    private Utils utils;

    private DisposableObserver<MovieResult> movieObserver;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        repository = ((BasicApp) application).getRepository();
        utils = ((BasicApp)application).getUtils();

        movieError = new MutableLiveData<>();
        movieLoader = new MutableLiveData<>();
    }

    public MutableLiveData<String> movieError() {
        return movieError;
    }

    public MutableLiveData<Boolean> movieLoader() {
        return movieLoader;
    }

    public LiveData<MovieResult> movieByIdResult() {
        if (movieResult == null) {
            movieResult = new MutableLiveData<>();
        }
        loadMovieById();
        return movieResult;
    }

    private void loadMovieById() {

        movieObserver = new DisposableObserver<MovieResult>() {
            @Override
            public void onComplete() {}

            @Override
            public void onNext(MovieResult movieResult) {
                MovieDetailViewModel.this.movieResult.postValue(movieResult);
                movieLoader.postValue(false);
            }

            @Override
            public void onError(Throwable e) {
                movieError.postValue(e.getMessage());
                movieLoader.postValue(false);
            }
        };

        // todo: should be a call to repository in which according to the internet connecting movies will fetching from server or local db

        repository.getPopularMovieById(utils.isConnectedToInternet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieObserver);
    }

}
