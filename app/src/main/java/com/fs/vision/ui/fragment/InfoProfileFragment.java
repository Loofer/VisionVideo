package com.fs.vision.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fs.vision.R;
import com.fs.vision.entity.PageDetailInfo;
import com.fs.vision.ui.activity.InfoDetailActivity;

/**
 * Created by Devil on 2016/10/26.
 */

public class InfoProfileFragment extends Fragment {


    private TextView videoInfoTxt;
    private PageDetailInfo pageDetailInfo;
    private InfoDetailActivity infoDetailActivity;
    private TextView infoProfileTxt;
    private TextView expandTxt;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        infoDetailActivity = (InfoDetailActivity) activity;
        infoDetailActivity.setProfileHandler(mHandler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videoInfoTxt = (TextView)view.findViewById(R.id.video_info_txt);
        infoProfileTxt = (TextView)view.findViewById(R.id.info_profile_txt);
        expandTxt = (TextView)view.findViewById(R.id.expand_txt);
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0://根据网络中获取的值更新界面
                    pageDetailInfo = (PageDetailInfo)msg.obj;
                    videoInfoTxt.setText(Html.fromHtml(pageDetailInfo.getVideoInfo()));
                    infoProfileTxt.setText(Html.fromHtml(pageDetailInfo.getSmallTxt()));
                    if (infoProfileTxt != null && expandTxt != null) {
                        //展开收缩简介
                        expandTxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (getResources().getString(R.string.txt_expand_open).equals(expandTxt.getText())) {//展开全部
                                    expandTxt.setText(getResources().getString(R.string.txt_expand_close));
                                    infoProfileTxt.setText(Html.fromHtml(pageDetailInfo.getAllTxt()));
                                } else if (getResources().getString(R.string.txt_expand_close).equals(expandTxt.getText())) {//收起简介
                                    expandTxt.setText(getResources().getString(R.string.txt_expand_open));
                                    infoProfileTxt.setText(Html.fromHtml(pageDetailInfo.getSmallTxt()));
                                }
                            }
                        });
                    }
                    break;
            }
        };
    };
}
