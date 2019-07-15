package com.example.al_gaith_customar.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.al_gaith_customar.R;

import static com.example.al_gaith_customar.MyApp.getProxy;

public class AboutVidioActivity extends AppCompatActivity {

    View loadView;
    VideoView videoView;
    int position = 0;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putInt("vid_position", position);
        Log.println(Log.ASSERT,"save",""+position);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.println(Log.ASSERT, "position", "" + position);

        videoView.start();

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        position = savedInstanceState.getInt("vid_position");
        Log.println(Log.ASSERT,"read",""+position);

        videoView.seekTo(position);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_vidio);


//        position = savedInstanceState.getInt("vid_position");
//        Log.println(Log.ASSERT, "position", "" + position);

        loadView = findViewById(R.id.animation_view);
        videoView = (VideoView) findViewById(R.id.about_video_view);
        //Use a media controller so that you can scroll the video contents
        //and also to pause, start the video.
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        HttpProxyCacheServer proxy = getProxy(this);
        String proxyUrl = proxy.getProxyUrl("http://alresalah-co.com/Ghaithdemo/VID/promo-Gh.mp4");

        videoView.setVideoURI(Uri.parse(proxyUrl));


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.println(Log.ASSERT, "vid prepared ", "prepared ");
                loadView.setVisibility(View.GONE);
              //  videoView.seekTo(2000);
            }
        });

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                Log.println(Log.ASSERT, "vid info ", "what: " + what);
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        position=videoView.getCurrentPosition();


    }
}
