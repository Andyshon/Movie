package com.andyshon.moviedb.data.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.andyshon.moviedb.data.BasicApp;
import com.andyshon.moviedb.data.MovieRepository;
import com.andyshon.moviedb.data.Utils;
import com.andyshon.moviedb.data.entity.Movie;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by andyshon on 06.08.18.
 */

public class MovieListViewModel extends AndroidViewModel {

    private MutableLiveData<Movie> movieResult;
    private MutableLiveData<String> movieError;
    private MutableLiveData<Boolean> movieLoader;

    private DisposableObserver<Movie> popularMoviesObserver;

    private MovieRepository mRepository;
    private Utils utils;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((BasicApp)application).getRepository();
        utils = ((BasicApp)application).getUtils();

        movieError = new MutableLiveData<>();
        movieLoader = new MutableLiveData<>();
        movieResult = new MutableLiveData<>();
        loadPopularMovies();
    }

    public LiveData<String> movieError() {
        return movieError;
    }

    public LiveData<Boolean> movieLoader() {
        return movieLoader;
    }

    public LiveData<Movie> movieResult() {
        return movieResult;
    }

    private void loadPopularMovies() {

        popularMoviesObserver = new DisposableObserver<Movie>() {
            @Override
            public void onComplete() {}

            @Override
            public void onNext(Movie movie) {
                movieResult.postValue(movie);
                movieLoader.postValue(false);
            }

            @Override
            public void onError(Throwable e) {
                movieError.postValue(e.getMessage());
                movieLoader.postValue(false);
            }
        };

        mRepository.getPopularMovies(utils.isConnectedToInternet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(popularMoviesObserver);
    }


    public void disposeElements() {
        if (popularMoviesObserver != null && !popularMoviesObserver.isDisposed()) popularMoviesObserver.dispose();
    }
}
