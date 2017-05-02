package com.fs.vision.entity;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by Devil on 2016/11/25.
 * 页面信息
 */
public class PageInfo extends RealmObject implements Parcelable {
    private String score;//评分
    private String type;//类型:(电影模式：高清、超清..;电视剧:更新到多少集)
    private String actor;//主演
    private String updatetime;//更新时间
    private String ahref;//视频超链接
    private String title;//视频名称
    private String year;//哪一年
    private String addr;//影片地区
    private String page;//页码
    private String imgUrl;//图片地址

    @Override
    public String toString() {
        return "PageInfo{" +
                "score='" + score + '\'' +
                ", type='" + type + '\'' +
                ", actor='" + actor + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", ahref='" + ahref + '\'' +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", addr='" + addr + '\'' +
                ", page='" + page + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }

    public PageInfo() {
    }

    public PageInfo(String score, String type, String actor, String updatetime, String ahref, String title, String year, String addr, String page,String imgUrl) {
        this.score = score;
        this.type = type;
        this.actor = actor;
        this.updatetime = updatetime;
        this.ahref = ahref;
        this.title = title;
        this.year = year;
        this.addr = addr;
        this.page = page;
        this.imgUrl = imgUrl;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getAhref() {
        return ahref;
    }

    public void setAhref(String ahref) {
        this.ahref = ahref;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.score);
        dest.writeString(this.type);
        dest.writeString(this.actor);
        dest.writeString(this.updatetime);
        dest.writeString(this.ahref);
        dest.writeString(this.title);
        dest.writeString(this.year);
        dest.writeString(this.addr);
        dest.writeString(this.page);
        dest.writeString(this.imgUrl);
    }

    protected PageInfo(Parcel in) {
        this.score = in.readString();
        this.type = in.readString();
        this.actor = in.readString();
        this.updatetime = in.readString();
        this.ahref = in.readString();
        this.title = in.readString();
        this.year = in.readString();
        this.addr = in.readString();
        this.page = in.readString();
        this.imgUrl = in.readString();
    }

    public static final Creator<PageInfo> CREATOR = new Creator<PageInfo>() {
        @Override
        public PageInfo createFromParcel(Parcel source) {
            return new PageInfo(source);
        }

        @Override
        public PageInfo[] newArray(int size) {
            return new PageInfo[size];
        }
    };

    //注意这里重写了equals方法
    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }else {
            if(this.getClass() == obj.getClass()){
                PageInfo pageInfo = (PageInfo) obj;
                if(this.getTitle().equals(pageInfo.getTitle())){
                    return true;
                }else{
                    return false;
                }

            }else{
                return false;
            }
        }
    }
}
