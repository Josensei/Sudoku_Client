package com.maco.clientejuegos.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;


public class ParcelableMessage implements Parcelable {

    private JSONObject jsm;

    public ParcelableMessage(JSONObject jsm) {
        this.jsm=jsm;
    }

    protected ParcelableMessage(Parcel in) {
    }

    public JSONObject getJsm() {
        return jsm;
    }

    public static final Creator<ParcelableMessage> CREATOR = new Creator<ParcelableMessage>() {
        @Override
        public ParcelableMessage createFromParcel(Parcel in) {
            return new ParcelableMessage(in);
        }

        @Override
        public ParcelableMessage[] newArray(int size) {
            return new ParcelableMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(this.jsm);
    }
}