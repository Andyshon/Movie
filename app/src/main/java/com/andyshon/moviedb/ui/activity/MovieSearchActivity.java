package com.andyshon.moviedb.ui.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.andyshon.moviedb.R;
import com.andyshon.moviedb.UserPreferences;
import com.andyshon.moviedb.Utils;
import com.andyshon.moviedb.data.entity.MovieSearch;
import com.andyshon.moviedb.data.entity.MovieSearchResult;
import com.andyshon.moviedb.ui.MovieSearchClickCallback;
import com.andyshon.moviedb.ui.adapter.MovieSearchListAdapter;
import com.andyshon.moviedb.ui.viewmodel.MovieSearchViewModel;

import io.reactivex.subjects.PublishSubject;

import static com.andyshon.moviedb.GlobalConstants.ApiConstants.*;

public class MovieSearchActivity extends AppCompatActivity implements MovieSearchClickCallback {

    private SearchView searchView;
    private MovieSearchListAdapter mAdapter;
    private ProgressBar progressBar;

    private MovieSearchViewModel viewModel;

    private String last_query;
    private String set_last_query;

    private PublishSubject<String> subject;

    private Context context;

    private LiveData<MovieSearch> movieSearchResult;
    private LiveData<String> movieSearchError;
    private LiveData<Boolean> movieSearchLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);

        CURRENT_PAGE = 1;

        context = MovieSearchActivity.this;

        last_query = UserPreferences.loadLastSearchQuery(this);

        progressBar = findViewById(R.id.progressbar);

        mAdapter = new MovieSearchListAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.recycler);

        RecyclerView.LayoutManager mLayoutManager;

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            mLayoutManager = new GridLayoutManager(this, 4);
        else mLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = findViewById(R.id.searchView);
        searchView.onActionViewExpanded();

        initSearchViewListener();

        viewModel = ViewModelProviders.of(this).get(MovieSearchViewModel.class);

        subscribe();

        setObservers();
    }


    private void setObservers() {
        movieSearchResult.observe(this, movieSearch -> {

            if (Utils.hasInternet(context))
                Toast.makeText(MovieSearchActivity.this, "Найдено " + movieSearch.getTotal_results() + " фильмов!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MovieSearchActivity.this, "Нет интернета. Показаны фильмы с последнего поиска:" + last_query, Toast.LENGTH_LONG).show();

            mAdapter.setMoviesList(movieSearch.getMovies());
        });

        movieSearchError.observe(this, s -> Toast.makeText(MovieSearchActivity.this,
                "Check Internet connection, no cached data.", Toast.LENGTH_LONG).show());

        movieSearchLoader.observe(this, aBoolean -> { if (!aBoolean) progressBar.setVisibility(View.GONE); });
    }


    private void subscribe() {
        movieSearchResult = viewModel.movieResult(subject);
        movieSearchError = viewModel.movieError();
        movieSearchLoader = viewModel.movieLoader();
    }


    private void initSearchViewListener() {

        subject = PublishSubject.create();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                subject.onComplete();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                if (text.isEmpty())
                    hideProgressbar();
                else {
                    showProgressbar();
                    set_last_query = text;
                }
                subject.onNext(text);
                return true;
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
        CURRENT_MOVIE_ID = movie.getId();

        Intent intent = new Intent(this, MovieDetailActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        if (Utils.hasInternet(context))
            UserPreferences.saveLastSearchQuery(this, set_last_query);
        viewModel.disposeElements();
        super.onDestroy();
    }
}
