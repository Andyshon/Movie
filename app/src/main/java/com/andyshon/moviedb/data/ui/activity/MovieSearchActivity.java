package com.andyshon.moviedb.data.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.andyshon.moviedb.R;
import com.andyshon.moviedb.data.entity.MovieSearchResult;
import com.andyshon.moviedb.data.remote.RestClient;
import com.andyshon.moviedb.data.ui.MovieSearchClickCallback;
import com.andyshon.moviedb.data.ui.adapter.MovieSearchListAdapter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        searchView = findViewById(R.id.searchView);
        searchView.onActionViewExpanded();

        searchViewSubject()
                .debounce(1000, TimeUnit.MILLISECONDS)
                .filter(text -> !text.isEmpty())
                .distinctUntilChanged()
                .switchMap(query -> RestClient.getService().getSearchMovies(API_KEY, LANGUAGE, query, 1, false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Toast.makeText(MovieSearchActivity.this, "Найдено " + result.getMovies().size() + " фильмов!", Toast.LENGTH_SHORT).show();
                    mAdapter.setMoviesList(result);
                    hideProgressbar();
                }, throwable -> {
                    Toast.makeText(MovieSearchActivity.this, "Ошибка во время поиска:" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    hideProgressbar();
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public Observable<String> searchViewSubject() {

        final PublishSubject<String> subject = PublishSubject.create();

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
                else showProgressbar();
                subject.onNext(text);
                return true;
            }
        });

        return subject;
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

//        Intent intent = new Intent();
//        setResult(RESULT_OK, intent);
//        finish();

        Intent intent = new Intent(this, MovieDetailActivity.class);
        startActivity(intent);
    }
}
