package com.fs.vision.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fs.vision.R;
import com.fs.vision.entity.SearchHistroy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devil on 2016/11/27.
 */
public class RecyclerTxtAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SearchHistroy> dataList = new ArrayList<>();

    public RecyclerTxtAdapter(Context context) {
        mContext = context;
    }

    public void addAllData(List<SearchHistroy> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    //清除列表所有数据
    public void clearData() {
        this.dataList.clear();
    }

    //内容视图中组件
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            itemTxt = (TextView) itemView.findViewById(R.id.item_txt);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_txt_item, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).itemTxt.setText(dataList.get(position).getField());
        if (onItemClickListener != null) {
            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });

            ((ViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
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

    private RecyclerTxtAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyclerTxtAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return dataList.size() == 0 ? 0 : dataList.size();
    }

}
