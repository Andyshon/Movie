package com.andyshon.moviedb.data.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.andyshon.moviedb.data.BasicApp;
import com.andyshon.moviedb.data.MovieRepository;
import com.andyshon.moviedb.data.entity.Movie;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by andyshon on 06.08.18.
 */

public class MovieListViewModel extends AndroidViewModel {

    private MutableLiveData<Movie> movieLiveData;
    private MutableLiveData<String> movieLiveDataError;

    private DisposableObserver<Movie> disposableObserver;

    private MovieRepository mRepository;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((BasicApp)application).getRepository();
    }


    public LiveData<Movie> fetchPopularMovies() {
        if (movieLiveData == null) {
            movieLiveData = new MutableLiveData<>();
            loadPopularMovies();
        }
        return movieLiveData;
    }

    private void loadPopularMovies() {

        disposableObserver = new DisposableObserver<Movie>() {
            @Override
            public void onNext(Movie movieResults) {
                movieLiveData.postValue(movieResults);
            }

            @Override
            public void onError(Throwable e) {
                if (movieLiveDataError == null) {
                    movieLiveDataError = new MutableLiveData<>();
                }
                movieLiveDataError.postValue(e.getMessage());
            }

            @Override
            public void onComplete() {
                // no-opt
            }
        };

        // todo: should be a call to repository in which according to the internet connecting movies will fetching from server or local db

        mRepository.fetchPopularMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(disposableObserver);
    }

}
