package com.ajit.appstreetdemo.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Flicker implements Parcelable {
    public static final Creator<Flicker> CREATOR = new Creator<Flicker>() {
        @Override
        public Flicker createFromParcel(Parcel source) {
            Flicker var = new Flicker();
            var.stat = source.readString();
            var.photos = source.readParcelable(FlickerPhotos.class.getClassLoader());
            return var;
        }

        @Override
        public Flicker[] newArray(int size) {
            return new Flicker[size];
        }
    };
    private String stat;
    private FlickerPhotos photos;

    public String getStat() {
        return this.stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public FlickerPhotos getPhotos() {
        return this.photos;
    }

    public void setPhotos(FlickerPhotos photos) {
        this.photos = photos;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stat);
        dest.writeParcelable(this.photos, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
