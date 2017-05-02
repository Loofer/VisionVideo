package com.fs.vision.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fs.vision.R;

public class SplashActivity extends AppCompatActivity {

    private TextView txtCountTime;
    private Handler mHandler;
    private int time = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        txtCountTime = (TextView) findViewById(R.id.txtCountTime);
        txtCountTime.setText(time+"s");
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 1000);
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(time > 1){
                mHandler.postDelayed(this, 1000);
                time = time - 1;
                txtCountTime.setText(time+"s");
            }else{
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }
    };
}
