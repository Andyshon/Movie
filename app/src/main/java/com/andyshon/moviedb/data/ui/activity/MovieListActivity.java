package com.andyshon.moviedb.data.ui.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andyshon.moviedb.R;
import com.andyshon.moviedb.data.GlobalConstants;
import com.andyshon.moviedb.data.entity.Movie;
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.data.ui.MovieClickCallback;
import com.andyshon.moviedb.data.ui.adapter.MovieListAdapter;
import com.andyshon.moviedb.data.ui.viewmodel.MovieListViewModel;

import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.*;

public class MovieListActivity extends AppCompatActivity implements MovieClickCallback {

    private MovieListViewModel viewModel;
    private MovieListAdapter mAdapter;
    private ProgressBar progressBar;
    private TextView tvCurrentPage;

    private LiveData<Movie> movieResult;
    private LiveData<String> movieError;
    private LiveData<Boolean> movieLoader;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 10;
    private boolean isFirstPage = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CURRENT_PAGE = 1;

//        deleteDatabase("movie-db");

        viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);


        progressBar = findViewById(R.id.progressbar);
        tvCurrentPage = findViewById(R.id.tvCurrentPage);

        mAdapter = new MovieListAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.recycler);

        RecyclerView.LayoutManager mLayoutManager;

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            mLayoutManager = new GridLayoutManager(this, 4);
        else mLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new PagingScrollListener() {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                CURRENT_PAGE += 1;

                isFirstPage = false;
                subscribe();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        subscribe();

        movieResult.observe(this, movie -> {

            if (!isFirstPage) {
                mAdapter.removeLoadingFooter();
                isLoading = false;

                mAdapter.addAll(movie.getMovies());

                if (CURRENT_PAGE != TOTAL_PAGES) mAdapter.addLoadingFooter();
                else showLastPageLimit();

                tvCurrentPage.setText(String.valueOf(GlobalConstants.ApiConstants.CURRENT_PAGE));
            }
            else {
                CURRENT_PAGE = 1;

                progressBar.setVisibility(View.GONE);
                mAdapter.addAll(movie.getMovies());

                if (CURRENT_PAGE <= TOTAL_PAGES) mAdapter.addLoadingFooter();
                else showLastPageLimit();
            }
        });

        movieError.observe(this, s -> Toast.makeText(MovieListActivity.this, "Error:" + s, Toast.LENGTH_LONG).show());

        movieLoader.observe(this, aBoolean -> { if (!aBoolean) progressBar.setVisibility(View.GONE); });
    }


    private void showLastPageLimit() {
        isLastPage = true;
        Toast.makeText(this, "You reached the last page.", Toast.LENGTH_SHORT).show();
    }


    private void subscribe() {
        movieResult = viewModel.movieResult();
        movieError = viewModel.movieError();
        movieLoader = viewModel.movieLoader();
    }


    @Override
    protected void onDestroy() {
        viewModel.disposeElements();
        super.onDestroy();
    }


    public void startDetailActivity() {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(MovieResult movie) {
        startDetailActivity();
    }


    public void search(View view) {
        Intent intent = new Intent(MovieListActivity.this, MovieSearchActivity.class);
        startActivity(intent);
    }


    public void nextPage(View view) {
        CURRENT_PAGE++;
        subscribe();
    }


    public void previousPage(View view) {
        if (CURRENT_PAGE != 1) {
            CURRENT_PAGE--;
            subscribe();
        }
    }
}
