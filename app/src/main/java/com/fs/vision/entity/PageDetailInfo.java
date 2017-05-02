package com.fs.vision.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Devil on 2016/11/25.
 * 页面详情
 */
public class PageDetailInfo extends RealmObject implements Parcelable {

    /**
     * 时间
     */
    private long datetime;

    /**
     * 页面链接
     */
    private String Url;
    /**
     * 标题
     */
    private String title;
    /**
     * 封面链接
     */
    private String cover;
    /**
     * 剧情介绍
     */
    private String smallTxt;
    private String allTxt;

    /**
     * 影片信息(状态、类型、导演、年份、语言、地区、更新时间)
     */
    private String videoInfo;
    /**
     * 下载地址
     */
    @Ignore
    List<List> downList;

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    /**
     * 搜索信息
     */
    private PageInfo pageInfo;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSmallTxt() {
        return smallTxt;
    }

    public void setSmallTxt(String smallTxt) {
        this.smallTxt = smallTxt;
    }

    public String getAllTxt() {
        return allTxt;
    }

    public void setAllTxt(String allTxt) {
        this.allTxt = allTxt;
    }

    public String getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(String videoInfo) {
        this.videoInfo = videoInfo;
    }

    public List<List> getDownList() {
        return downList;
    }

    public void setDownList(List<List> downList) {
        this.downList = downList;
    }

    @Override
    public String toString() {
        return "PageDetailInfo{" +
                "Url='" + Url + '\'' +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", smallTxt='" + smallTxt + '\'' +
                ", allTxt='" + allTxt + '\'' +
                ", videoInfo='" + videoInfo + '\'' +
                ", downList='" + downList + '\'' +
//                ", pageInfo=" + pageInfo.toString() +
                '}';
    }

    /**
     * 页面详情构造
     *
     * @param url
     * @param title
     * @param cover
     * @param smallTxt
     * @param allTxt
     * @param videoInfo
     * @param downList
     * @param pageInfo  pageinfo对象
     */
    public PageDetailInfo(String url, String title, String cover, String smallTxt, String allTxt, String videoInfo, List<List> downList, PageInfo pageInfo) {
        this.Url = url;
        this.title = title;
        this.cover = cover;
        this.smallTxt = smallTxt;
        this.allTxt = allTxt;
        this.videoInfo = videoInfo;
        this.downList = downList;
        this.pageInfo = pageInfo;
    }

    /**
     * 页面详情构造
     *
     * @param url
     * @param title
     * @param cover
     * @param smallTxt
     * @param allTxt
     * @param videoInfo
     * @param downList
     */
    public PageDetailInfo(String url, String title, String cover, String smallTxt, String allTxt, String videoInfo, List<List> downList) {
        this.Url = url;
        this.title = title;
        this.cover = cover;
        this.smallTxt = smallTxt;
        this.allTxt = allTxt;
        this.videoInfo = videoInfo;
        this.downList = downList;
    }

    public PageDetailInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.datetime);
        dest.writeString(this.Url);
        dest.writeString(this.title);
        dest.writeString(this.cover);
        dest.writeString(this.smallTxt);
        dest.writeString(this.allTxt);
        dest.writeString(this.videoInfo);
        dest.writeList(this.downList);
        dest.writeParcelable(this.pageInfo, flags);
    }

    protected PageDetailInfo(Parcel in) {
        this.datetime = in.readLong();
        this.Url = in.readString();
        this.title = in.readString();
        this.cover = in.readString();
        this.smallTxt = in.readString();
        this.allTxt = in.readString();
        this.videoInfo = in.readString();
        this.downList = new ArrayList<List>();
        in.readList(this.downList, List.class.getClassLoader());
        this.pageInfo = in.readParcelable(PageInfo.class.getClassLoader());
    }

    public static final Creator<PageDetailInfo> CREATOR = new Creator<PageDetailInfo>() {
        @Override
        public PageDetailInfo createFromParcel(Parcel source) {
            return new PageDetailInfo(source);
        }

        @Override
        public PageDetailInfo[] newArray(int size) {
            return new PageDetailInfo[size];
        }
    };
}
