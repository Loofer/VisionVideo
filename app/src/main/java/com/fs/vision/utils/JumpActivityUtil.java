package com.fs.vision.utils;

/**
 * Created by Devil on 2016/11/25.
 */
public class JumpActivityUtil {

    /**
     * 跳转到页面详情PageInfo(搜索，首页)
     *
     * @param position       adapter的position
     * @param packageContext 上下文
     * @param mAdapter       adapter
     */
//    public static <E extends RecyclerArrayAdapter<PageInfo>> void JumpPageDetail(int position, Context packageContext, E mAdapter) {
//        Intent intent = new Intent(packageContext, act_seasondetail.class);
//        intent.putExtra(IntenConstant.pagedetailurl, mAdapter.getItem(position).getAhref());
//        intent.putExtra(IntenConstant.pageInfo, mAdapter.getItem(position));
//        packageContext.startActivity(intent);
//    }
//
//
//    /**
//     * 跳转到页面详情PageDetailInfo(收藏页面)
//     *
//     * @param position       adapter的position
//     * @param packageContext 上下文
//     * @param mAdapter       adapter
//     */
//    public static <E extends OrderedRealmCollection<PageDetailInfo>> void JumpPageDetail2(int position, Context packageContext, E mAdapter) {
//        Intent intent = new Intent(packageContext, act_seasondetail.class);
//        intent.putExtra(IntenConstant.pagedetailurl, mAdapter.get(position).getPageInfo().getAhref());
//        intent.putExtra(IntenConstant.pageInfo, mAdapter.get(position).getPageInfo());
//        packageContext.startActivity(intent);
//    }
}
