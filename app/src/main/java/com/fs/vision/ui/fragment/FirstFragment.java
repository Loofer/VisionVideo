package com.fs.vision.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fs.vision.R;
import com.fs.vision.adapter.RecyclerGalleryAdapter;
import com.fs.vision.constant.Constant;
import com.fs.vision.entity.PageInfo;
import com.fs.vision.net.VideoNetApi;
import com.fs.vision.ui.activity.InfoDetailActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devil on 2016/10/26.
 */

public class FirstFragment extends Fragment {

    private RecyclerGalleryAdapter mRecyclerGalleryAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private List<PageInfo> data = new ArrayList<>();
    private Handler handler = new Handler();
    private boolean isLoading = false;
    private Thread thread;
    private File cacheDir;
    private int currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_swiperefresh, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cacheDir = getActivity().getCacheDir();

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvList);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.holo_orange_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefresh(getActivity()));
        getData(false, currentPage, VideoNetApi.typeFilm);
        mRecyclerGalleryAdapter = new RecyclerGalleryAdapter(getActivity());
        mRecyclerGalleryAdapter.addAllData(data);

        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mRecyclerGalleryAdapter.getItemViewType(position)) {
                    case RecyclerGalleryAdapter.TYPE_ITEM:
                        return 1;
                    case RecyclerGalleryAdapter.TYPE_FOOTER:
                        return 3;

                    default:
                        return 1;
                }
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mRecyclerGalleryAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //加载更多
                if (!isLoading&& mRecyclerGalleryAdapter.getItemCount() == (mLayoutManager
                        .findLastVisibleItemPosition() + 1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    currentPage = currentPage + 1;
                    getData(false, currentPage, VideoNetApi.typeFilm);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //解决RecyclerView和SwipeRefreshLayout共用存在的bug
                mSwipeRefreshLayout.setEnabled(mLayoutManager
                        .findFirstCompletelyVisibleItemPosition() == 0);
            }
        });

        mRecyclerGalleryAdapter.setOnItemClickListener(new RecyclerGalleryAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                PageInfo pageInfo = mRecyclerGalleryAdapter.getItemPageInfo(position);
                Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
                intent.putExtra(Constant.pageDetailUrl, pageInfo.getAhref());
                intent.putExtra(Constant.pageInfo, pageInfo);
                getActivity().startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

        });
    }

    class SwipeRefreshLayoutOnRefresh implements SwipeRefreshLayout.OnRefreshListener {
        public SwipeRefreshLayoutOnRefresh(FragmentActivity activity) {

        }

        @Override
        public void onRefresh() {
            currentPage = 1;
            getData(true, currentPage, VideoNetApi.typeFilm);
            mSwipeRefreshLayout.setRefreshing(false);//下拉刷新完成后隐藏刷新图标
        }
    }

    /**
     * 获取数据
     *
     * @param page 页码
     * @param type 类型
     */
    private void getData(final boolean isClear, final int page, final String type) {
        //未打开wifi或连接有问题
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //未打开wifi或连接有问题
                final List<PageInfo> pageInfos = VideoNetApi.NewInstans(cacheDir).GetPage(page, type);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
                        if (pageInfos == null) {//未打开wifi或连接有问题
                            return;
                        } else {
                            if (isClear) {
                                mRecyclerGalleryAdapter.clearData();
                            }
                            mRecyclerGalleryAdapter.addAllData(pageInfos);

                        }
                    }
                });
            }
        });
        thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(thread != null){
            thread.interrupt();
            thread = null;
        }
    }
}
