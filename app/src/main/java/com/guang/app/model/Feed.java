package com.guang.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 社交Feed流
 * Created by xiaoguang on 2018/1/1.
 */
public class Feed implements Parcelable {
    public String content;
    public ArrayList<String> photos;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeStringList(this.photos);
    }

    public Feed() {
    }

    public Feed(String content, ArrayList<String> photos) {
        this.content = content;
        this.photos = photos;
    }

    protected Feed(Parcel in) {
        this.content = in.readString();
        this.photos = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel source) {
            return new Feed(source);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };
}