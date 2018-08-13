package com.andyshon.moviedb.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.andyshon.moviedb.data.BasicApp;
import com.andyshon.moviedb.data.MovieRepository;
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.data.entity.MovieSearchResult;
import com.andyshon.moviedb.data.entity.MovieTrailer;

import javax.inject.Inject;

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

    private MutableLiveData<MovieTrailer> trailerResult;
    private MutableLiveData<String> trailerError;
    private MutableLiveData<Boolean> trailerLoader;

    private DisposableObserver<MovieResult> movieObserver;
    private DisposableObserver<MovieTrailer> trailerObserver;


    private MutableLiveData<MovieSearchResult> movieSearchResult;
    private MutableLiveData<String> movieSearchError;
    private MutableLiveData<Boolean> movieSearchLoader;
    private DisposableObserver<MovieSearchResult> movieSearchObserver;

    @Inject
    MovieRepository mRepository;


    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = BasicApp.getApp().getActivityComponent().movieRepository();

        movieError = new MutableLiveData<>();
        movieLoader = new MutableLiveData<>();
        movieResult = new MutableLiveData<>();
        loadMovieById();

        trailerError = new MutableLiveData<>();
        trailerLoader = new MutableLiveData<>();
        trailerResult = new MutableLiveData<>();
        loadTrailers();

        movieSearchError = new MutableLiveData<>();
        movieSearchLoader = new MutableLiveData<>();
        movieSearchResult = new MutableLiveData<>();
    }

    public LiveData<String> movieError() {
        return movieError;
    }

    public LiveData<Boolean> movieLoader() {
        return movieLoader;
    }

    public LiveData<MovieResult> movieByIdResult() {
        return movieResult;
    }

    private void loadMovieById() {

        movieObserver = new DisposableObserver<MovieResult>() {
            @Override
            public void onComplete() {}

            @Override
            public void onNext(MovieResult result) {
                movieResult.postValue(result);
                movieLoader.postValue(false);
            }

            @Override
            public void onError(Throwable e) {
                movieError.postValue(e.getMessage());
                movieLoader.postValue(false);
            }
        };

        mRepository.getMovieById()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieObserver);
    }



    public LiveData<String> movieSearchError() {
        return movieSearchError;
    }

    public LiveData<Boolean> movieSearchLoader() {
        return movieSearchLoader;
    }

    public LiveData<MovieSearchResult> movieSearchByIdResult() {
        loadMovieSearchById();
        return movieSearchResult;
    }

    private void loadMovieSearchById() {

        movieSearchObserver = new DisposableObserver<MovieSearchResult>() {
            @Override
            public void onComplete() {}

            @Override
            public void onNext(MovieSearchResult result) {
                movieSearchResult.postValue(result);
                movieSearchLoader.postValue(false);
            }

            @Override
            public void onError(Throwable e) {
                movieSearchError.postValue(e.getMessage());
                movieSearchLoader.postValue(false);
            }
        };

        mRepository.getMovieSearchById()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieSearchObserver);
    }



    public LiveData<String> trailerError() {
        return trailerError;
    }

    public LiveData<Boolean> trailerLoader() {
        return trailerLoader;
    }

    public LiveData<MovieTrailer> trailerResult() {
        return trailerResult;
    }

    private void loadTrailers() {
        trailerObserver = new DisposableObserver<MovieTrailer>() {
            @Override
            public void onComplete() {}

            @Override
            public void onNext(MovieTrailer result) {
                trailerResult.postValue(result);
                trailerLoader.postValue(false);
            }

            @Override
            public void onError(Throwable e) {
                trailerError.postValue(e.getMessage());
                trailerLoader.postValue(false);
            }
        };

        mRepository.getTrailerByMovieId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trailerObserver);
    }


    public void disposeElements() {
        if (movieObserver != null && !movieObserver.isDisposed()) movieObserver.dispose();
        if (trailerObserver != null && !trailerObserver.isDisposed()) trailerObserver.dispose();
        if (movieSearchObserver != null && !movieSearchObserver.isDisposed()) movieSearchObserver.dispose();
    }
}
