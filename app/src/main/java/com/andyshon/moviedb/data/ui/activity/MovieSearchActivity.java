package com.andyshon.moviedb.data.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.andyshon.moviedb.R;
import com.andyshon.moviedb.data.entity.MovieSearch;
import com.andyshon.moviedb.data.entity.MovieSearchResult;
import com.andyshon.moviedb.data.remote.RestClient;
import com.andyshon.moviedb.data.ui.MovieSearchClickCallback;
import com.andyshon.moviedb.data.ui.adapter.MovieSearchListAdapter;

import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.*;

public class MovieSearchActivity extends AppCompatActivity implements MovieSearchClickCallback {

    private SearchView searchView;
    private MovieSearchListAdapter mAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);


        mAdapter = new MovieSearchListAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        searchView = findViewById(R.id.searchView);





        RxSearchObservable.fromView(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String text) throws Exception {
                        return !text.isEmpty();
                    }
                })
                .distinctUntilChanged()
                .switchMap(new Function<String, ObservableSource<MovieSearch>>() {
                    @Override
                    public ObservableSource<MovieSearch> apply(String query) throws Exception {
                        return RestClient.getService().getSearchMovies(API_KEY, LANGUAGE, query, 1, false);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieSearch>() {
                    @Override
                    public void accept(MovieSearch result) throws Exception {
                        Toast.makeText(MovieSearchActivity.this, "find " + result.getMovies().size() + " movies!", Toast.LENGTH_SHORT).show();
                        mAdapter.setMoviesList(result);
                        hideProgressbar();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MovieSearchActivity.this, "error :D" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        hideProgressbar();
                    }
                });
    }


    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(MovieSearchResult movie) {
        Toast.makeText(this, movie.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
