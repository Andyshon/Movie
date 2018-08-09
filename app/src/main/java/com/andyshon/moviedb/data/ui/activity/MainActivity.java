package com.andyshon.moviedb.data.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andyshon.moviedb.R;
import com.andyshon.moviedb.data.GlobalConstants;
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.data.remote.RestClient;
import com.andyshon.moviedb.data.ui.MovieClickCallback;
import com.andyshon.moviedb.data.ui.adapter.MovieListAdapter;
import com.andyshon.moviedb.data.ui.viewmodel.MovieListViewModel;

import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.*;

public class MainActivity extends AppCompatActivity implements MovieClickCallback {

    private MovieListViewModel viewModel;
    private MovieListAdapter mAdapter;
    private ProgressBar progressBar;
    private TextView tvCurrentPage;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //deleteDatabase("movie-db");

        RestClient.initService();

        viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);


        progressBar = findViewById(R.id.progressbar);
        tvCurrentPage = findViewById(R.id.tvCurrentPage);

        mAdapter = new MovieListAdapter(this);
        recyclerView = findViewById(R.id.recycler);

        RecyclerView.LayoutManager mLayoutManager;

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            mLayoutManager = new GridLayoutManager(this, 4);
        else mLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        subscribeUI();
    }


    private void subscribeUI() {

        viewModel.movieResult().observe(this, movie -> {
            System.out.println("subscribeUI movieResult");
            if (movie != null) mAdapter.setMoviesList(movie.getMovies());
            tvCurrentPage.setText(String.valueOf(GlobalConstants.ApiConstants.CURRENT_PAGE));
            recyclerView.scrollToPosition(0);
        });

        viewModel.movieError().observe(this, s -> Toast.makeText(MainActivity.this, "Error:" + s, Toast.LENGTH_LONG).show());

        viewModel.movieLoader().observe(this, aBoolean -> { if (!aBoolean) progressBar.setVisibility(View.GONE); });
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
        Intent intent = new Intent(MainActivity.this, MovieSearchActivity.class);
        startActivityForResult(intent, 999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            startDetailActivity();
        }
    }

    public void nextPage(View view) {
        CURRENT_PAGE++;
        subscribeUI();
    }

    public void previousPage(View view) {
        if (CURRENT_PAGE != 1) {
            CURRENT_PAGE--;
            subscribeUI();
        }
    }
}
