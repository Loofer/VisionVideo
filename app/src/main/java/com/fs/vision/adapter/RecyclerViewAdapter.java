package com.fs.vision.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fs.vision.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devil on 2016/11/27.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<String> dataList = new ArrayList<>();
    private DisplayMetrics dm;

    public RecyclerViewAdapter(Context context) {
        mContext = context;
        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    // 设置展示的是内容视图还是底部视图
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addAllData(List<String> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    //清除列表所有数据
    public void clearData() {
        this.dataList.clear();
    }

    //内容视图中组件
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTxt;

        public ViewHolder(View itemView1) {
            super(itemView1);
            nameTxt = (TextView) itemView1.findViewById(R.id.name_txt);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_btn_item, parent,
                false);
        return new RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).nameTxt.setText(dataList.get(position+1).split("\\*")[0]);
        if (onItemClickListener != null) {
            ((ViewHolder) holder).nameTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });

            ((ViewHolder) holder).nameTxt.setOnLongClickListener(new View.OnLongClickListener() {
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

    private RecyclerViewAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyclerViewAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public int getItemCount() {
        return dataList.size() == 0 ? 0 : dataList.size()-1;
    }
}
