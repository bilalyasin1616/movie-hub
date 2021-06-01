package com.eg.moviehub;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.halilibo.bettervideoplayer.BetterVideoPlayer;

public class VideoTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_test);

        BetterVideoPlayer videoPlayer=findViewById(R.id.video_player);
        videoPlayer.setSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
        videoPlayer.start();
    }
}
