package com.example.myapplicationrecycle_view;

import android.os.Parcel;
import android.os.Parcelable;

public class MyPair implements Parcelable {
    public String Date;
    public int Value;

    public MyPair(String date, int value){
        Date = date;
        Value = value;
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Date);
        dest.writeInt(Value);
    }

    private MyPair(Parcel in) {
        Date = in.readString();
        Value = in.readInt();
    }

    public static final Parcelable.Creator<MyPair> CREATOR
            = new Parcelable.Creator<MyPair>() {
        public MyPair createFromParcel(Parcel in) {
            return new MyPair(in);
        }

        public MyPair[] newArray(int size) {
            return new MyPair[size];
        }
    };
}