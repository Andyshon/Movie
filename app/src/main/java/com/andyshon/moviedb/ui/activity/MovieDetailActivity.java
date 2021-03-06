package com.andyshon.moviedb.ui.activity;

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
import com.andyshon.moviedb.GlobalConstants;
import com.andyshon.moviedb.data.entity.MovieResult;
import com.andyshon.moviedb.data.entity.MovieSearchResult;
import com.andyshon.moviedb.data.entity.MovieTrailer;
import com.andyshon.moviedb.data.entity.MovieTrailerResult;
import com.andyshon.moviedb.ui.viewmodel.MovieDetailViewModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.andyshon.moviedb.GlobalConstants.ApiConstants.TRAILER_START_SECONDS;

public class MovieDetailActivity extends AppCompatActivity implements YouTubeListenerImpl.ListenerCallback {

    private MovieDetailViewModel viewModel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView ivImage;
    private TextView tvTitle, tvSubTitle, tv1, tv2, tv3, tv4, tvNoTrailers;
    private ProgressBar progressBar;
    private LinearLayout trailersLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<YouTubePlayerView> youTubePlayerViewList;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerListener mYoutubeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this::subscribe);


        tvTitle = findViewById(R.id.tvTitle);
        tvSubTitle = findViewById(R.id.tvSubtitle);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tvNoTrailers = findViewById(R.id.tvNoTrailers);
        tvNoTrailers.setVisibility(View.GONE);

        ivImage = findViewById(R.id.image);

        progressBar = findViewById(R.id.progressbar);
        trailersLayout = findViewById(R.id.trailersLayout);

        youTubePlayerViewList = new ArrayList<>();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);

        subscribe();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void subscribe() {

        viewModel.movieByIdResult().observe(this, this::updateUIPopular);
        viewModel.movieLoader().observe(this, aBoolean -> { if (!aBoolean) progressBar.setVisibility(View.GONE); swipeRefreshLayout.setRefreshing(false); });
        viewModel.movieError().observe(this, error -> {
            // нет такого фильма в популярных. ищем его в movie search result
            subscribeMovieSearch();
        });


        viewModel.trailerResult().observe(this, this::updateTrailersUI);
        viewModel.trailerLoader().observe(this, aBoolean -> { if (!aBoolean) progressBar.setVisibility(View.GONE); swipeRefreshLayout.setRefreshing(false); });
        viewModel.trailerError().observe(this, s -> tvNoTrailers.setVisibility(View.VISIBLE));
    }


    private void subscribeMovieSearch() {
        viewModel.movieSearchByIdResult().observe(this, this::updateUISearch);
        viewModel.movieSearchError().observe(this, error1 -> Toast.makeText(MovieDetailActivity.this, "Error:" + error1, Toast.LENGTH_SHORT).show());
        viewModel.movieSearchLoader().observe(this, aBoolean -> { if (!aBoolean) progressBar.setVisibility(View.GONE); swipeRefreshLayout.setRefreshing(false); });
    }


    private void updateUIPopular(MovieResult movieResult) {

        collapsingToolbarLayout.setTitle(movieResult.getTitle());
        tvTitle.setText(movieResult.getTitle());
        tvSubTitle.setText("Описание: ".concat(movieResult.getOverview()));
        tv1.setText("Рейтинг: ".concat(String.valueOf(movieResult.getVote_count())));
        tv2.setText("Популярность: ".concat(String.valueOf(movieResult.getPopularity())));
        tv3.setText("Дата выхода: ".concat(movieResult.getRelease_date().substring(0,4)));
        tv4.setText("Средняя оценка: ".concat(String.valueOf(movieResult.getVote_average())));

        if (movieResult.getBackdrop_path() != null) {
            String image_proper_size = GlobalConstants.getImageSize(this, true).getSize();
            String imagePath = GlobalConstants.ApiConstants.IMAGE_PATH.concat(image_proper_size).concat("/").concat(movieResult.getBackdrop_path());
            Picasso.get().load(imagePath).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_foreground).into(ivImage);
        }
    }


    private void updateUISearch(MovieSearchResult movieSearchResult) {

        collapsingToolbarLayout.setTitle(movieSearchResult.getTitle());
        tvTitle.setText(movieSearchResult.getTitle());
        tvSubTitle.setText("Описание: ".concat(movieSearchResult.getOverview()));
        tv1.setText("Рейтинг: ".concat(String.valueOf(movieSearchResult.getVote_count())));
        tv2.setText("Популярность: ".concat(String.valueOf(movieSearchResult.getPopularity())));
        if (movieSearchResult.getRelease_date().length() >= 4) {
            tv3.setText("Дата выхода: ".concat(movieSearchResult.getRelease_date().substring(0, 4)));
        }
        else {
            tv3.setText("Дата выхода: ".concat(movieSearchResult.getRelease_date()));
        }
        tv4.setText("Средняя оценка: ".concat(String.valueOf(movieSearchResult.getVote_average())));

        if (movieSearchResult.getBackdrop_path() != null) {
            String image_proper_size = GlobalConstants.getImageSize(this, true).getSize();
            String imagePath = GlobalConstants.ApiConstants.IMAGE_PATH.concat(image_proper_size).concat("/").concat(movieSearchResult.getBackdrop_path());
            Picasso.get().load(imagePath).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_foreground).into(ivImage);
        }
    }


    private void updateTrailersUI(MovieTrailer movieTrailer) {
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
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="
                            .concat(trailer.getKey()).concat("&t=").concat(String.valueOf(getStartSeconds(trailer)).concat("s"))));
                    intent.putExtra("force_fullscreen", true);
                    startActivityForResult(intent, 222);
                }

                @Override
                public void onYouTubePlayerExitFullScreen() {}
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
                    mYoutubeListener = new YouTubeListenerImpl(this, trailer);
                    youTubePlayer.addListener(mYoutubeListener);

                    initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady() {
                            initializedYouTubePlayer.cueVideo(trailer.getKey(), getStartSeconds(trailer));
                        }
                    });
                }, true);
    }


    public float getStartSeconds(MovieTrailerResult trailer) {
        float startSeconds = 0f;
        if (TRAILER_START_SECONDS.containsKey(trailer.getKey()))
            startSeconds = TRAILER_START_SECONDS.get(trailer.getKey());
        return startSeconds;
    }


    @Override
    public void getCurrentSecond(MovieTrailerResult trailer, float startSeconds) {
        TRAILER_START_SECONDS.put(trailer.getKey(), startSeconds);
    }


    @Override
    protected void onDestroy() {
        if (youTubePlayer != null)
            youTubePlayer.removeListener(mYoutubeListener);
        viewModel.disposeElements();
        super.onDestroy();
        for (YouTubePlayerView playerView : youTubePlayerViewList) {
            playerView.release();
        }
        youTubePlayerViewList.clear();
    }
}
