package com.andyshon.moviedb.data.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andyshon.moviedb.R;
import com.andyshon.moviedb.data.GlobalConstants;
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.data.ui.viewmodel.MovieDetailViewModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private MovieDetailViewModel viewModel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView ivImage, ivBack;
    private TextView tvTitle, tvSubTitle, tv1, tv2;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        tvTitle = findViewById(R.id.tvTitle);
        tvSubTitle = findViewById(R.id.tvSubtitle);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);

        ivImage = findViewById(R.id.image);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(view -> finish());

        progressBar = findViewById(R.id.progressbar);

        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);

        initYoutube();

        subscribeUI();

    }

    private void initYoutube() {
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(
                initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        initializedYouTubePlayer.cueVideo("LHvYrn3FAgI", 0);
                    }
                }), true);
    }

    private void subscribeUI() {
        viewModel.movieByIdResult().observe(this, this::updateUI);

        viewModel.movieError().observe(this, s -> Toast.makeText(MovieDetailActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show());

        viewModel.movieLoader().observe(this, aBoolean -> {
            if (!aBoolean) progressBar.setVisibility(View.GONE);
        });
    }

    private void updateUI(MovieResult movie) {
        collapsingToolbarLayout.setTitle(movie.getTitle());
        tvTitle.setText(movie.getTitle());
        tvSubTitle.setText("Описание: ".concat(movie.getOverview()));
        tv1.setText("Рейтинг: ".concat(String.valueOf(movie.getVote_count())));
        tv2.setText("Популярность: ".concat(String.valueOf(movie.getPopularity())));
        String imagePath = GlobalConstants.ApiConstants.IMAGE_PATH_W500.concat(movie.getBackdrop_path());
        Picasso.get().load(imagePath).into(ivImage);
    }
}
