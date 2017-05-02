package com.fs.vision.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fs.vision.R;
import com.fs.vision.constant.Constant;
import com.fs.vision.net.VideoNetApi;

import tcking.github.com.giraffeplayer.GiraffePlayer;

public class VideoPlayActivity extends AppCompatActivity{

    private GiraffePlayer player;
    private LinearLayout playLLayout;
    private String videoPlayHtml;
    private String videoPlayUrl;
    private Handler mHandler = new Handler();
    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        getIntentData();
        initVideoPlay();
        getVideoInfoDetail();
    }

    private void getIntentData() {
        videoPlayHtml = getIntent().getStringExtra(Constant.videoPlayHtml);
    }

    private void initVideoPlay() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        playLLayout = (LinearLayout) findViewById(R.id.play_lLayout);
        int playLLayoutHeight = screenWidth * 3 / 4;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, playLLayoutHeight);
        playLLayout.setLayoutParams(layoutParams);
        String url = "https://yundied.duapp.com/player2.php?vid=NADmUT0CNxjwQI3qNhAcO0O0OoO0O0OY~a206349a.acfun";
        if (player == null) {
            player = new GiraffePlayer(VideoPlayActivity.this);
            player.setScaleType(GiraffePlayer.SCALETYPE_FITPARENT);
//            player.play(url);
        }
    }

    private void getVideoInfoDetail() {
        //访问网络
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                videoPlayUrl = VideoNetApi.NewInstans(getCacheDir()).GetIframeUrl(videoPlayHtml);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (player != null) {
                            player.play(videoPlayUrl.replaceAll("&amp;", "&"));
                        }
                    }
                });
            }
        });
        mThread.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (player != null) {
                player.stop();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (player != null) {
            player.onDestroy();
        }
    }

}
