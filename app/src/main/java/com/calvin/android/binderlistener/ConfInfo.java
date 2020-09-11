package com.calvin.android.binderlistener;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:cl
 * Email:lhzheng@grandstream.cn
 * Date:20-9-11
 */
public class ConfInfo implements Parcelable {
    private String id;
    private String name;

    public static final Creator<ConfInfo> CREATOR = new Creator<ConfInfo>() {
        @Override
        public ConfInfo createFromParcel(Parcel source) {
            return new ConfInfo(source);
        }

        @Override
        public ConfInfo[] newArray(int size) {
            return new ConfInfo[size];
        }
    };

    public ConfInfo(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
    }


    public ConfInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    @Override
    public String toString() {
        return "ConfInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
