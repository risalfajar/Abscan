package com.example.risalfajar.abscan.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class AbsenData implements Parcelable {
    private int id;
    private String datetime;
    private String nim;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.datetime);
        dest.writeString(this.nim);
    }

    public AbsenData() {
    }

    protected AbsenData(Parcel in) {
        this.id = in.readInt();
        this.datetime = in.readString();
        this.nim = in.readString();
    }

    public static final Creator<AbsenData> CREATOR = new Creator<AbsenData>() {
        @Override
        public AbsenData createFromParcel(Parcel source) {
            return new AbsenData(source);
        }

        @Override
        public AbsenData[] newArray(int size) {
            return new AbsenData[size];
        }
    };
}
