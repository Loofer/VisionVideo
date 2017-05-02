package com.fs.vision.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fs.vision.R;
import com.fs.vision.adapter.RecyclerGalleryAdapter;
import com.fs.vision.adapter.RecyclerTxtAdapter;
import com.fs.vision.constant.Constant;
import com.fs.vision.db.realm.DbSearchHis;
import com.fs.vision.entity.PageInfo;
import com.fs.vision.entity.SearchHistroy;
import com.fs.vision.net.VideoNetApi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Devil on 2016/12/1.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout searchLLayout, historyLLayout, resultLLayout;
    private EditText searchEdit;
    private TextView cancelTxt, clearTxt;
    private RecyclerView historyRecycler, resultRecycler;
    private Thread thread;
    private File cacheDir;
    private Handler handler = new Handler();
    private List<PageInfo> data = new ArrayList<>();
    private RecyclerGalleryAdapter mRecyclerGalleryAdapter;
    private boolean isLoading = false;
    private int currentPage = 1;
    private String searchWord = "";
    private DbSearchHis dbSearchHis;
    private RecyclerTxtAdapter mRecyclerTxtAdapter;
    private RealmResults<SearchHistroy> searchHistroyList;
    private boolean isGetAllData = false;
    private List<PageInfo> tempInfo = new ArrayList<PageInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        cacheDir = this.getCacheDir();
        dbSearchHis = DbSearchHis.builder(this, DbSearchHis.FIELD);

        initView();
        initHistroyRecyclerView();
        initResultRecyclerView();
    }

    private void initView() {
        searchLLayout = (LinearLayout) findViewById(R.id.search_lLayout);
        searchEdit = (EditText) findViewById(R.id.search_edit);
        cancelTxt = (TextView) findViewById(R.id.cancel_txt);
        cancelTxt.setOnClickListener(this);
        historyLLayout = (LinearLayout) findViewById(R.id.history_lLayout);
        clearTxt = (TextView) findViewById(R.id.clear_txt);
        clearTxt.setOnClickListener(this);
        historyRecycler = (RecyclerView) findViewById(R.id.history_recycler);
        resultLLayout = (LinearLayout) findViewById(R.id.result_lLayout);
        resultRecycler = (RecyclerView) findViewById(R.id.result_recycler);

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) searchEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    currentPage = 1;
                    mRecyclerGalleryAdapter.clearData();
                    searchWord = searchEdit.getText().toString();
                    if (!dbSearchHis.findIsItemExit(searchWord)) {
                        SearchHistroy searchHistroy = new SearchHistroy();
                        searchHistroy.setField(searchWord);
                        dbSearchHis.insert(searchHistroy);
                    }
                    getData(currentPage, searchWord);
                    return true;
                }
                return false;
            }
        });
    }

    private void initHistroyRecyclerView() {
        historyLLayout.setVisibility(View.VISIBLE);
        resultLLayout.setVisibility(View.GONE);
        searchHistroyList = dbSearchHis.findAll();
        mRecyclerTxtAdapter = new RecyclerTxtAdapter(SearchActivity.this);
        mRecyclerTxtAdapter.addAllData(searchHistroyList);
        historyRecycler.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        historyRecycler.setAdapter(mRecyclerTxtAdapter);

        mRecyclerTxtAdapter.setOnItemClickListener(new RecyclerTxtAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                currentPage = 1;
                mRecyclerGalleryAdapter.clearData();
                searchWord = searchHistroyList.get(position).getField();
                getData(currentPage, searchWord);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

        });
    }

    private void initResultRecyclerView() {
        mRecyclerGalleryAdapter = new RecyclerGalleryAdapter(SearchActivity.this);
        mRecyclerGalleryAdapter.addAllData(data);

        final GridLayoutManager mLayoutManager = new GridLayoutManager(SearchActivity.this, 3);
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

        resultRecycler.setLayoutManager(mLayoutManager);

        resultRecycler.setAdapter(mRecyclerGalleryAdapter);
        resultRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //加载更多
                if (!isLoading && mRecyclerGalleryAdapter.getItemCount() == (mLayoutManager
                        .findLastVisibleItemPosition() + 1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE && !isGetAllData) {
                    currentPage = currentPage + 1;
                    getData(currentPage, searchWord);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        mRecyclerGalleryAdapter.setOnItemClickListener(new RecyclerGalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PageInfo pageInfo = mRecyclerGalleryAdapter.getItemPageInfo(position);
                Intent intent = new Intent(SearchActivity.this, InfoDetailActivity.class);
                intent.putExtra(Constant.pageDetailUrl, pageInfo.getAhref());
                intent.putExtra(Constant.pageInfo, pageInfo);
                SearchActivity.this.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

        });
    }

    /**
     * 获取数据
     *
     * @param page       页码
     * @param searchWord 搜索关键字
     */
    private void getData(final int page, final String searchWord) {
        //未打开wifi或连接有问题
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //未打开wifi或连接有问题
                final List<PageInfo> pageInfos = VideoNetApi.NewInstans(cacheDir).SearchPage(page, searchWord);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
                        if (pageInfos == null) {//未获取到页面数据
                            return;
                        } else {
                            historyLLayout.setVisibility(View.GONE);
                            resultLLayout.setVisibility(View.VISIBLE);
                            if (tempInfo.size() == 0 && pageInfos.size() < 5) {
                                isGetAllData = true;
                                mRecyclerGalleryAdapter.setFootShow(false);
                                mRecyclerGalleryAdapter.addAllData(pageInfos);
                            } else if (tempInfo.size() > 0 && tempInfo.containsAll(pageInfos) && pageInfos.containsAll(tempInfo)) {
                                isGetAllData = true;
                                mRecyclerGalleryAdapter.notifyMoreFinish(mRecyclerGalleryAdapter.getItemCount());
                            } else {
                                if (page == 1 && pageInfos.size() >= 5) {
                                    currentPage = currentPage + 1;
                                    getData(currentPage, searchWord);
                                }
                                mRecyclerGalleryAdapter.setFootShow(true);
                                mRecyclerGalleryAdapter.addAllData(pageInfos);
                                isGetAllData = false;
                                tempInfo.clear();
                                tempInfo.addAll(pageInfos);
                            }
//                            if(pageInfos.size() < 5){
//                                isGetAllData = true;
//                                mRecyclerGalleryAdapter.notifyMoreFinish(mRecyclerGalleryAdapter.getItemCount());
//                            }else {
//                                mRecyclerGalleryAdapter.setFootShow(true);
//                                isGetAllData = false;
//                            }
                        }
                    }
                });
            }
        });
        thread.start();
    }

    private boolean compare(List<PageInfo> pageInfoList1, List<PageInfo> pageInfoList2) {
        for(PageInfo pageInfo1:pageInfoList1){
            for (PageInfo pageInfo2:pageInfoList2){
                if(pageInfo1.toString() != pageInfo2.toString()){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_txt:
                finish();
                break;

            case R.id.clear_txt:
                mRecyclerTxtAdapter.clearData();
                dbSearchHis.deleteAll();
                mRecyclerTxtAdapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }
}
