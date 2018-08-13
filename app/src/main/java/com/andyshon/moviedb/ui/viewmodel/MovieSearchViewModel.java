package com.andyshon.moviedb.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.andyshon.moviedb.data.BasicApp;
import com.andyshon.moviedb.data.MovieRepository;
import com.andyshon.moviedb.data.entity.MovieSearch;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by andyshon on 10.08.18.
 */

public class MovieSearchViewModel extends AndroidViewModel {

    private MutableLiveData<MovieSearch> movieSearchResult;
    private MutableLiveData<String> movieSearchError;
    private MutableLiveData<Boolean> movieSearchLoader;

    private DisposableObserver<MovieSearch> searchMoviesObserver;

    @Inject
    MovieRepository mRepository;

    public MovieSearchViewModel(@NonNull Application application) {
        super(application);
        mRepository = BasicApp.getApp().getActivityComponent().movieRepository();

        movieSearchError = new MutableLiveData<>();
        movieSearchLoader = new MutableLiveData<>();
        movieSearchResult = new MutableLiveData<>();
    }

    public LiveData<String> movieError() {
        return movieSearchError;
    }

    public LiveData<Boolean> movieLoader() {
        return movieSearchLoader;
    }

    public LiveData<MovieSearch> movieResult(PublishSubject<String> subject) {
        loadSearchMovies(subject);
        return movieSearchResult;
    }

    private void loadSearchMovies(PublishSubject<String> subject) {

        searchMoviesObserver = new DisposableObserver<MovieSearch>() {
            @Override
            public void onComplete() {}

            @Override
            public void onNext(MovieSearch movieSearch) {
                movieSearchResult.postValue(movieSearch);
                movieSearchLoader.postValue(false);
            }

            @Override
            public void onError(Throwable e) {
                movieSearchError.postValue(e.getMessage());
                movieSearchLoader.postValue(false);
            }
        };

        subject.debounce(1000, TimeUnit.MILLISECONDS)
                .filter(text -> !text.isEmpty())
                .distinctUntilChanged()
                .switchMap(query -> mRepository.getSearchMovies(query))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchMoviesObserver);
    }

    public void disposeElements() {
        if (searchMoviesObserver != null && !searchMoviesObserver.isDisposed()) searchMoviesObserver.dispose();
    }
}