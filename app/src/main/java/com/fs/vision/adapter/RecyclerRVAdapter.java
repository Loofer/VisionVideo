package com.fs.vision.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fs.vision.R;
import com.fs.vision.widget.slm.GridSLM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devil on 2016/11/27.
 */
public class RecyclerRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_HEADER = 0;//悬浮顶部视图
    public static final int VIEW_TYPE_CONTENT = 1;//主内容视图

    private Context mContext;
    private ArrayList<LineItem> mItems;
    private int mHeaderDisplay;
    private boolean mMarginsFixed = false;

    public RecyclerRVAdapter(Context context, int headerMode) {
        mContext = context;
        mHeaderDisplay = headerMode;
    }

    public void addAllData(List<List> dataList) {
        mItems = new ArrayList<>();

        int sectionFirstPosition = 0;
        for (int i = 0; i < dataList.size(); i++) {
            for (int j = 0; j <  dataList.get(i).size(); j++) {
                String title = dataList.get(i).get(j).toString().split("\\*")[0];
                if(j == 0){
                    sectionFirstPosition = mItems.size();
                    mItems.add(new LineItem(title, true, sectionFirstPosition,null));
                }else {
                    String urlStr = dataList.get(i).get(j).toString().split("\\*")[1];
                    mItems.add(new LineItem(title, false, sectionFirstPosition,urlStr));
                }
            }
        }
        notifyDataSetChanged();
    }

    //内容视图中组件
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView stickyHeaderTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            stickyHeaderTxt = (TextView) itemView.findViewById(R.id.sticky_header_txt);
        }

        public void bindItem(String text) {
            stickyHeaderTxt.setText(text);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_hearder_item, parent, false);
        } else if (viewType == VIEW_TYPE_CONTENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_line_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final LineItem item = mItems.get(position);
        final View itemView = holder.itemView;

        ((ViewHolder)holder).bindItem(item.title);

        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        if (item.isHeader) {
            lp.headerDisplay = mHeaderDisplay;
            if (lp.isHeaderInline() || (mMarginsFixed && !lp.isHeaderOverlay())) {
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            lp.headerEndMarginIsAuto = !mMarginsFixed;
            lp.headerStartMarginIsAuto = !mMarginsFixed;
        }
        lp.setSlm(GridSLM.ID);
        lp.setColumnWidth(mContext.getResources().getDimensionPixelSize(R.dimen.grid_column_width));
        lp.setFirstPosition(item.sectionFirstPosition);
        itemView.setLayoutParams(lp);

      if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private RecyclerRVAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyclerRVAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public boolean isItemHeader(int position) {
        return mItems.get(position).isHeader;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    public String getItemViewContent(int position) {
        return mItems.get(position).urlStr;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private static class LineItem {

        public int sectionFirstPosition;
        public boolean isHeader;
        public String title;
        public String urlStr;

        public LineItem(String title, boolean isHeader, int sectionFirstPosition,String urlStr) {
            this.isHeader = isHeader;
            this.title = title;
            this.sectionFirstPosition = sectionFirstPosition;
            this.urlStr = urlStr;
        }
    }

}
