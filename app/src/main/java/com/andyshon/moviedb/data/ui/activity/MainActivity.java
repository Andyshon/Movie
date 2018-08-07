package com.andyshon.moviedb.data.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andyshon.moviedb.R;
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.data.remote.RestClient;
import com.andyshon.moviedb.data.ui.MovieClickCallback;
import com.andyshon.moviedb.data.ui.adapter.MovieListAdapter;
import com.andyshon.moviedb.data.ui.viewmodel.MovieListViewModel;

public class MainActivity extends AppCompatActivity implements MovieClickCallback {

    private MovieListViewModel viewModel;
    private MovieListAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RestClient.initService();

        viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);


        progressBar = findViewById(R.id.progressbar);

        adapter = new MovieListAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        subscribeUI();
    }


    private void subscribeUI() {

        viewModel.movieResult().observe(this, movie -> {
            if (movie != null) adapter.setMoviesList(movie.getMovies());
        });

        viewModel.movieError().observe(this, s -> Toast.makeText(MainActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show());

        viewModel.movieLoader().observe(this, aBoolean -> {
            if (!aBoolean) progressBar.setVisibility(View.GONE);
        });
    }


    @Override
    public void onClick(MovieResult movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        startActivity(intent);
    }
}
