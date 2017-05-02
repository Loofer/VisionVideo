package com.fs.vision.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fs.vision.R;
import com.fs.vision.adapter.FragmentAdapter;
import com.fs.vision.constant.Constant;
import com.fs.vision.entity.PageDetailInfo;
import com.fs.vision.entity.PageInfo;
import com.fs.vision.net.VideoNetApi;
import com.fs.vision.ui.fragment.InfoProfileFragment;
import com.fs.vision.ui.fragment.InfoResourceFragment;
import com.fs.vision.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class InfoDetailActivity extends AppCompatActivity {

    private String pageDetailUrl;
    private PageInfo pageInfo;
    private CollapsingToolbarLayout detailToolbarLayout;
    private ViewPager detailViewpager;
    private TabLayout detailTabLayout;
    private ImageView detailToolbarIv;
    private FragmentAdapter adapter;
    private Thread mThread;
    private Handler mHandler = new Handler();
    private PageDetailInfo pageDetailInfo;
    private Handler profileFragmentHandler,resourceFragmentHandler;
    private Toolbar detailToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);

        getIntentData();
        setupToolbar();
        setupViewPager();
        setupCollapsingToolbar();

        getVideoInfoDetail();
    }

    private void getIntentData() {
        pageDetailUrl = getIntent().getStringExtra(Constant.pageDetailUrl);
        pageInfo = getIntent().getParcelableExtra(Constant.pageInfo);
    }

    private void getVideoInfoDetail() {
        //访问网络
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                pageDetailInfo = VideoNetApi.NewInstans(getCacheDir()).GetPageDetail(pageDetailUrl);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (pageDetailInfo == null) {//显示网络异常
                            return;
                        }
                        Message profileMsg = new Message();
                        profileMsg.what = 0;
                        profileMsg.obj = pageDetailInfo;
                        profileFragmentHandler.sendMessage(profileMsg);

                        Message resourceMsg = new Message();
                        resourceMsg.what = 0;
                        resourceMsg.obj = pageDetailInfo;
                        resourceFragmentHandler.sendMessage(resourceMsg);
                    }
                });
            }
        });
        mThread.start();
    }

    private void setupCollapsingToolbar() {
        detailToolbarLayout = (CollapsingToolbarLayout) findViewById(
                R.id.detail_toolbarLayout);
        detailToolbarLayout.setExpandedTitleMarginEnd(DensityUtil.dip2px(this, 12.0f));
        detailToolbarLayout.setExpandedTitleMarginStart(DensityUtil.dip2px(this, 12.0f));

        detailToolbarIv = (ImageView) findViewById(R.id.detail_toolbar_iv);
        Glide.with(this)
                .load(pageInfo.getImgUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(detailToolbarIv);
    }

    private void setupViewPager() {
        detailViewpager = (ViewPager) findViewById(R.id.detail_viewpager);
        detailTabLayout = (TabLayout) findViewById(R.id.detail_tabLayout);
        detailTabLayout.setupWithViewPager(detailViewpager);
        setupViewPager(detailViewpager);
    }

    private void setupToolbar() {
        detailToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        detailToolbar.setTitle(pageInfo.getTitle());
        setSupportActionBar(detailToolbar);
        detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewPager(ViewPager mViewPager) {
        List<String> titles = new ArrayList<>();
        titles.add("简介");
        titles.add("资源");
        detailTabLayout.addTab(detailTabLayout.newTab().setText(titles.get(0)));
        detailTabLayout.addTab(detailTabLayout.newTab().setText(titles.get(1)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new InfoProfileFragment());
        fragments.add(new InfoResourceFragment());
        adapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        detailTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
         adapter = null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
    }

    public void setProfileHandler(Handler mHandler) {
        profileFragmentHandler = mHandler;
    }

    public void setResourceHandler(Handler mHandler) {
        resourceFragmentHandler = mHandler;
    }
}
