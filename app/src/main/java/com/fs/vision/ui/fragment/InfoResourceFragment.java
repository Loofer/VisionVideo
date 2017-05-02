package com.fs.vision.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fs.vision.R;
import com.fs.vision.adapter.RecyclerRVAdapter;
import com.fs.vision.constant.Constant;
import com.fs.vision.entity.PageDetailInfo;
import com.fs.vision.ui.activity.InfoDetailActivity;
import com.fs.vision.ui.activity.VideoPlayActivity;
import com.fs.vision.widget.slm.GridSLM;
import com.fs.vision.widget.slm.LayoutManager;
import com.fs.vision.widget.slm.SectionLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Devil on 2016/10/26.
 */

public class InfoResourceFragment extends Fragment {


    private RecyclerView resourceRecyclerView;
    private InfoDetailActivity infoDetailActivity;
    private PageDetailInfo pageDetailInfo;
    private RecyclerRVAdapter mRecyclerRVAdapter;
    private List<List> data = new ArrayList<>();

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        infoDetailActivity = (InfoDetailActivity) activity;
        infoDetailActivity.setResourceHandler(mHandler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_resource, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resourceRecyclerView = (RecyclerView)view.findViewById(R.id.resource_recyclerView);
        initRecyclerView();
    }


    private void initRecyclerView() {
        mRecyclerRVAdapter = new RecyclerRVAdapter(getActivity(),getResources().getInteger(R.integer.default_header_display));
        mRecyclerRVAdapter.addAllData(data);
        resourceRecyclerView.setAdapter(mRecyclerRVAdapter);
        resourceRecyclerView.setLayoutManager(new LayoutManager(getActivity()));
        mRecyclerRVAdapter.setOnItemClickListener(new RecyclerRVAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                if(mRecyclerRVAdapter.getItemViewType(position) != mRecyclerRVAdapter.VIEW_TYPE_HEADER){
                    Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                    intent.putExtra(Constant.videoPlayHtml,mRecyclerRVAdapter.getItemViewContent(position));
                    getActivity().startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

        });
    }

    /**
     * 获取测试数据
     */
    private void getData() {
        int s = data.size();
        for (int i = 0; i < 5; i++) {
            List<String> list = new ArrayList<>();
            list.add("来源\n测试"+(i+s));
            data.add(list);
        }
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0://根据网络中获取的值更新界面
                    pageDetailInfo = (PageDetailInfo)msg.obj;
                    mRecyclerRVAdapter.addAllData(pageDetailInfo.getDownList());
                    break;
            }
        };
    };
}
