package com.calvin.android.binderlistener;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:cl
 * Email:lhzheng@grandstream.cn
 * Date:20-9-11
 */
public class SubscriberInfo implements Parcelable {

    private String id;
    private String name;

    public static final Creator<SubscriberInfo> CREATOR = new Creator<SubscriberInfo>() {
        @Override
        public SubscriberInfo createFromParcel(Parcel source) {
            return new SubscriberInfo(source);
        }

        @Override
        public SubscriberInfo[] newArray(int size) {
            return new SubscriberInfo[size];
        }
    };

    public SubscriberInfo(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
    }

    public SubscriberInfo(String id, String name) {
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
        return "SubscriberInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
