package com.fs.vision.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fs.vision.R;
import com.fs.vision.entity.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devil
 * on 2015/7/2.
 */
public class RecyclerGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_ITEM = 0;//显示内容视图
    public static final int TYPE_FOOTER = 1;//显示底部加载视图
    private boolean isFooterShow = false;//是否显示加载更多
    private Context mContext;
    private List<PageInfo> dataList = new ArrayList<PageInfo>();

    public RecyclerGalleryAdapter(Context context) {
        mContext = context;
    }

   // 设置展示的是内容视图还是底部视图
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount() && isFooterShow) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void addAllData(List<PageInfo> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    //清除列表所有数据
    public void clearData() {
        this.dataList.clear();
    }

    //获取点击的item中的pageInfo信息
    public PageInfo getItemPageInfo(int position) {
        return dataList.get(position);
    }


    //内容视图中组件
    class ContentViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemIv;
        private TextView itemTitle;

        public ContentViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            itemIv = (ImageView) itemView.findViewById(R.id.item_iv);
        }
    }

    //底部加载视图中组件
    class FootViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar footLoadPBar;
        private final TextView footTxt;

        public FootViewHolder(View view) {
            super(view);
            footLoadPBar = (ProgressBar) itemView.findViewById(R.id.foot_load_pBar);
            footTxt = (TextView) itemView.findViewById(R.id.foot_txt);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {//内容视图
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_gallery_item, parent,
                    false);
            return new ContentViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {//底部视图
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_load_more_layout, parent,
                    false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).itemTitle.setText(dataList.get(position).getTitle());
            //图片缓存加载Glide方法
            Glide.with(mContext)
                    .load(dataList.get(position).getImgUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .override(480, 640)
                    .centerCrop()
                    .error(R.drawable.error_img2)
                    .into(((ContentViewHolder) holder).itemIv);

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
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        int count =dataList.size();
        if(isFooterShow){
            count++;
        }
        return count;
    }

    /**
     * 设置是否显示加载更多
     * @param isShow
     */
    public void setFootShow(boolean isShow) {
        isFooterShow = isShow;
    }


    /**
     * 已加载全部数据
     * @param count
     */
    public void notifyMoreFinish(int count) {
        setFootShow(false);
        notifyItemRemoved(count);
        notifyItemRangeChanged(1,count);
    }

}