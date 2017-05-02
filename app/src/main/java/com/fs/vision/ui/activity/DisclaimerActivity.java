package com.fs.vision.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fs.vision.R;
import com.fs.vision.constant.Constant;

/**
 * Created by Devil on 2016/12/30.
 */

public class DisclaimerActivity extends Activity implements View.OnClickListener {

    private ImageView backIv;
    private TextView titleTv;

    private String titleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        getIntentData();
        initView();
    }

    private void getIntentData() {
        titleTxt = getIntent().getStringExtra(Constant.titleTxt);
    }

    private void initView() {
        backIv = (ImageView)findViewById(R.id.backIv);
        backIv.setOnClickListener(this);
        titleTv = (TextView)findViewById(R.id.titleTv);
        titleTv.setText(titleTxt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backIv:
                finish();
                break;

            default:
                break;
        }
    }
}
