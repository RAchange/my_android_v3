package com.example.myapplicationrecycle_view;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Data_Information implements Parcelable {
    public String Name;
    public String Unit;
    public int HighValue;
    public ArrayList<MyPair> list = new ArrayList<>();

    public Data_Information(String name, String unit, int highValue) {
        Name = name;
        Unit = unit;
        HighValue = highValue;
    }

    public void add(String ss, int v) {
        MyPair M;
        M = new MyPair(ss, v);
        list.add(M);
    }

    public String getStr() {
        return Name;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Unit);
        dest.writeInt(HighValue);
        dest.writeTypedList(list);
    }

    private Data_Information(Parcel in) {
        Name = in.readString();
        Unit = in.readString();
        HighValue = in.readInt();
        in.readTypedList(list, MyPair.CREATOR);
    }

    public static final Parcelable.Creator<Data_Information> CREATOR
            = new Parcelable.Creator<Data_Information>() {
        public Data_Information createFromParcel(Parcel in) {
            return new Data_Information(in);
        }

        public Data_Information[] newArray(int size) {
            return new Data_Information[size];
        }
    };
}
