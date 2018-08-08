package com.andyshon.moviedb.data.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andyshon.moviedb.R;
import com.andyshon.moviedb.data.GlobalConstants;
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.data.entity.MovieSearchResult;
import com.andyshon.moviedb.data.entity.MovieTrailer;
import com.andyshon.moviedb.data.entity.MovieTrailerResult;
import com.andyshon.moviedb.data.ui.viewmodel.MovieDetailViewModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.andyshon.moviedb.data.GlobalConstants.ApiConstants.TRAILER_START_SECONDS;

public class MovieDetailActivity extends AppCompatActivity implements YouTubeListener.ListenerCallback {

    private MovieDetailViewModel viewModel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView ivImage;
    private TextView tvTitle, tvSubTitle, tv1, tv2, tvNoTrailers;
    private ProgressBar progressBar;
    private LinearLayout trailersLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<YouTubePlayerView> youTubePlayerViewList;
    private YouTubePlayer youTubePlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this::subscribeUI);


        tvTitle = findViewById(R.id.tvTitle);
        tvSubTitle = findViewById(R.id.tvSubtitle);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tvNoTrailers = findViewById(R.id.tvNoTrailers);
        tvNoTrailers.setVisibility(View.GONE);

        ivImage = findViewById(R.id.image);

        progressBar = findViewById(R.id.progressbar);
        trailersLayout = findViewById(R.id.trailersLayout);

        youTubePlayerViewList = new ArrayList<>();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);

        subscribeUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void subscribeUI() {

        viewModel.movieByIdResult().observe(this, this::updateUI);
        viewModel.movieError().observe(this, s -> Toast.makeText(MovieDetailActivity.this, "Error111:" + s, Toast.LENGTH_LONG).show());
        viewModel.movieLoader().observe(this, aBoolean -> { if (!aBoolean) progressBar.setVisibility(View.GONE); swipeRefreshLayout.setRefreshing(false); });

        viewModel.trailerResult().observe(this, this::updateTrailersUI);
        viewModel.trailerError().observe(this, s -> tvNoTrailers.setVisibility(View.VISIBLE));
        viewModel.trailerLoader().observe(this, aBoolean -> { if (!aBoolean) progressBar.setVisibility(View.GONE); swipeRefreshLayout.setRefreshing(false); });
    }

    private void updateUI(MovieResult movie) {
        System.out.println("updateUI");
        collapsingToolbarLayout.setTitle(movie.getTitle());
        tvTitle.setText(movie.getTitle());
        tvSubTitle.setText("Описание: ".concat(movie.getOverview()));
        tv1.setText("Рейтинг: ".concat(String.valueOf(movie.getVote_count())));
        tv2.setText("Популярность: ".concat(String.valueOf(movie.getPopularity())));
        String imagePath = GlobalConstants.ApiConstants.IMAGE_PATH_W500.concat(movie.getBackdrop_path());
        Picasso.get().load(imagePath).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_foreground).into(ivImage);
    }

    private void updateTrailersUI(MovieTrailer movieTrailer) {
        System.out.println("updateTrailersUI");
        trailersLayout.removeAllViews();
        youTubePlayerViewList.clear();

        tvNoTrailers.setVisibility(View.GONE);
        for (MovieTrailerResult trailer : movieTrailer.getTrailers()) {
            YouTubePlayerView youTubePlayerView = new YouTubePlayerView(getApplicationContext());
            MovieDetailActivity.this.getLifecycle().addObserver(youTubePlayerView);
            youTubePlayerView.setPadding(0, 20, 0, 20);

            youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
                @Override
                public void onYouTubePlayerEnterFullScreen() {
                    Toast.makeText(MovieDetailActivity.this, "onYouTubePlayerEnterFullScreen", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="
                            .concat(trailer.getKey()).concat("&t=").concat(String.valueOf(trailer.getKey()).concat("s"))));
                    intent.putExtra("force_fullscreen", true);
                    startActivityForResult(intent, 222);
                }

                @Override
                public void onYouTubePlayerExitFullScreen() {
                    Toast.makeText(MovieDetailActivity.this, "onYouTubePlayerExitFullScreen", Toast.LENGTH_SHORT).show();
                }
            });

            youTubePlayerViewList.add(youTubePlayerView);
            TextView textView = new TextView(MovieDetailActivity.this);
            textView.setText(trailer.getName());
            textView.setPadding(0, 10, 0, 0);
            trailersLayout.addView(textView);
            trailersLayout.addView(youTubePlayerView);
            initYouTubePlayerView(youTubePlayerView, trailer);
        }
    }



    private void initYouTubePlayerView(YouTubePlayerView youTubePlayerView, MovieTrailerResult trailer) {
        youTubePlayerView.initialize(
                initializedYouTubePlayer -> {
                    youTubePlayer = initializedYouTubePlayer;
                    YouTubeListener mListener = new YouTubeListener(this, trailer);
                    youTubePlayer.addListener(mListener.getYouTubePlayerListener());


                    initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady() {
                            float startSeconds = 0f;
                            if (TRAILER_START_SECONDS.containsKey(trailer.getKey())) {
                                startSeconds = TRAILER_START_SECONDS.get(trailer.getKey());
                            }
                            initializedYouTubePlayer.cueVideo(trailer.getKey(), startSeconds);
                        }
                    });
                }, true);
    }

    @Override
    protected void onDestroy() {
        viewModel.disposeElements();
        super.onDestroy();
        for (YouTubePlayerView playerView : youTubePlayerViewList) {
            playerView.release();
        }
        youTubePlayerViewList.clear();
    }

    @Override
    public void getCurrentSecond(MovieTrailerResult trailer, float startSeconds) {
        TRAILER_START_SECONDS.put(trailer.getKey(), startSeconds);
    }
}
