package com.eg.moviehub;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;

/**
 * Created by EG on 3/17/2019.
 */

public class PlayVideoActivity extends Activity {
    BetterVideoPlayer player;
    int seekTo=-1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        player= findViewById(R.id.video_player);
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        storageReference.child("videos/"+Globals.selectedVideo.id+".mp4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("Download URI", "onSuccess: "+uri.toString());
                Uri test=Uri.parse("https://goonj.pk/2942ddd4-4962-4302-b0f3-4b17200b9f4c");
                player.setSource(test);
                player.enableDoubleTapGestures(10000);
                player.enableSwipeGestures();
                if(seekTo!=-1)
                    player.seekTo(seekTo);
                player.start();
            }
        });

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String currentTime=savedInstanceState.getString("currentTime");
        Log.i("TestRestore", "onRestoreInstanceState: "+Integer.parseInt(currentTime));
        seekTo=Integer.parseInt(currentTime);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("TestStore", "onSaveInstanceState: "+player.getCurrentPosition());
        outState.putString("currentTime",String.valueOf(player.getCurrentPosition()));

    }
}
