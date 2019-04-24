package com.ajit.appstreetdemo.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FlickerPhotosPhoto implements Parcelable {
    public static final Creator<FlickerPhotosPhoto> CREATOR = new Creator<FlickerPhotosPhoto>() {
        @Override
        public FlickerPhotosPhoto createFromParcel(Parcel source) {
            FlickerPhotosPhoto var = new FlickerPhotosPhoto();
            var.owner = source.readString();
            var.server = source.readString();
            var.ispublic = source.readInt();
            var.isfriend = source.readInt();
            var.farm = source.readInt();
            var.id = source.readString();
            var.secret = source.readString();
            var.title = source.readString();
            var.isfamily = source.readInt();
            return var;
        }

        @Override
        public FlickerPhotosPhoto[] newArray(int size) {
            return new FlickerPhotosPhoto[size];
        }
    };
    private String owner;
    private String server;
    private int ispublic;
    private int isfriend;
    private int farm;
    private String id;
    private String secret;
    private String title;
    private int isfamily;

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getIspublic() {
        return this.ispublic;
    }

    public void setIspublic(int ispublic) {
        this.ispublic = ispublic;
    }

    public int getIsfriend() {
        return this.isfriend;
    }

    public void setIsfriend(int isfriend) {
        this.isfriend = isfriend;
    }

    public int getFarm() {
        return this.farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsfamily() {
        return this.isfamily;
    }

    public void setIsfamily(int isfamily) {
        this.isfamily = isfamily;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.owner);
        dest.writeString(this.server);
        dest.writeInt(this.ispublic);
        dest.writeInt(this.isfriend);
        dest.writeInt(this.farm);
        dest.writeString(this.id);
        dest.writeString(this.secret);
        dest.writeString(this.title);
        dest.writeInt(this.isfamily);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
