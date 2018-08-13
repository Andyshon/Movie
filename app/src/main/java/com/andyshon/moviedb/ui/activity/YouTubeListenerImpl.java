package com.andyshon.moviedb.ui.activity;

import android.support.annotation.NonNull;

import com.andyshon.moviedb.data.entity.MovieTrailerResult;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener;

/**
 * Created by andyshon on 08.08.18.
 */

public class YouTubeListenerImpl implements YouTubePlayerListener {

    private MovieTrailerResult trailer;
    private ListenerCallback callback;


    public interface ListenerCallback {
        void getCurrentSecond(MovieTrailerResult trailer, float startSeconds);
    }

    YouTubeListenerImpl(ListenerCallback _callback, MovieTrailerResult trailer) {
        this.trailer = trailer;
        this.callback = _callback;
    }

    @Override
    public void onCurrentSecond(float second) {
        callback.getCurrentSecond(trailer, second);
    }

    @Override
    public void onReady() {}

    @Override
    public void onStateChange(@NonNull PlayerConstants.PlayerState state) {}

    @Override
    public void onPlaybackQualityChange(@NonNull PlayerConstants.PlaybackQuality playbackQuality) {}

    @Override
    public void onPlaybackRateChange(@NonNull PlayerConstants.PlaybackRate playbackRate) {}

    @Override
    public void onError(@NonNull PlayerConstants.PlayerError error) {}

    @Override
    public void onApiChange() {}

    @Override
    public void onVideoDuration(float duration) {}

    @Override
    public void onVideoLoadedFraction(float loadedFraction) {}


    @Override
    public void onVideoId(@NonNull String videoId) {}
}
