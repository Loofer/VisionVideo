package com.fs.vision.entity;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by Devil on 2016/11/27.
 */

public class VideoUrlInfo extends RealmObject implements Parcelable {

    private String fromName;
    private String videoUrl;

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "VideoUrlInfo{" +
                "fromName='" + fromName + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }

    public VideoUrlInfo() {
    }

    public VideoUrlInfo(String fromName, String videoUrl) {
        this.fromName = fromName;
        this.videoUrl = videoUrl;
    }

    protected VideoUrlInfo(Parcel in) {
        fromName = in.readString();
        videoUrl = in.readString();
    }

    public static final Creator<VideoUrlInfo> CREATOR = new Creator<VideoUrlInfo>() {
        @Override
        public VideoUrlInfo createFromParcel(Parcel in) {
            return new VideoUrlInfo(in);
        }

        @Override
        public VideoUrlInfo[] newArray(int size) {
            return new VideoUrlInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fromName);
        dest.writeString(videoUrl);
    }
}
