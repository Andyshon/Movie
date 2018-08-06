package com.andyshon.moviedb.data.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.andyshon.moviedb.R;
import com.andyshon.moviedb.data.entity.MovieResults;
import com.andyshon.moviedb.data.remote.RestClient;
import com.andyshon.moviedb.data.ui.MovieClickCallback;
import com.andyshon.moviedb.data.ui.adapter.MovieListAdapter;
import com.andyshon.moviedb.data.ui.viewmodel.MovieListViewModel;

public class MainActivity extends AppCompatActivity implements MovieClickCallback {

    private MovieListViewModel viewModel;
    private MovieListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RestClient.initService();

        viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);


        adapter = new MovieListAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        subscribeUI();
    }

    private void subscribeUI() {
        viewModel.fetchPopularMovies().observe(this, movie -> {
            Toast.makeText(MainActivity.this, movie.getMovies().size() + " movies", Toast.LENGTH_SHORT).show();
            adapter.setMoviesList(movie.getMovies());
        });
    }

    @Override
    public void onClick(MovieResults movie) {
        Toast.makeText(this, movie.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
