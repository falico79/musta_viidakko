package com.leaves.nine.mustamets;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class MainActivity extends Activity {

    VideoView videoView;
    private MediaPlayer menuMusic;
    Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.SCREEN_WIDTH = dm.widthPixels;

        menuMusic = MediaPlayer.create(this, R.raw.menumusic);
        menuMusic.start();
        menuMusic.setLooping(true);
        setContentView(R.layout.layout_main_menu);
    }

    public void skipIntro(View view){
        videoView.stopPlayback();
        menuMusic.stop();
        setContentView(new GamePanel(Constants.CURRENT_CONTEXT));
    }

    public void new_game (View view) {
        Constants.CURRENT_CONTEXT = this;
        menuMusic.stop();
        UserInterface.resetValues();

        setContentView(R.layout.layout_intro_video);
        videoView = findViewById(R.id.videoViewIntro);
        String introVideoPath = "android.resource://" + getPackageName() + "/" + R.raw.introvideo;
        videoUri = Uri.parse(introVideoPath);
        videoView.setVideoURI(videoUri);
        videoView.setMediaController(null);

        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                setContentView(new GamePanel(Constants.CURRENT_CONTEXT));
            }
        });
    }

    public void continue_game (View view) {}

    public void exit_game (View view) {
        menuMusic.stop();
        this.finishAndRemoveTask();

    }
}
