package com.ajit.appstreetdemo.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "flicker_photos_photo_table")
public class FlickerPhotosPhoto implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    private int localId;

    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @NonNull
    @ColumnInfo(name = "search_text")
    private String searchText;
    @ColumnInfo(name = "owner")
    private String owner;
    @NonNull
    @ColumnInfo(name = "server")
    private String server;
    @ColumnInfo(name = "ispublic")
    private int ispublic;
    @ColumnInfo(name = "isfriend")
    private int isfriend;
    @ColumnInfo(name = "farm")
    private int farm;
    @NonNull
    @ColumnInfo(name = "secret")
    private String secret;
    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "isfamily")
    private int isfamily;

    public FlickerPhotosPhoto(int localId, @NonNull String id, @NonNull String searchText, String owner, @NonNull String server, int ispublic, int isfriend, int farm, @NonNull String secret, @NonNull String title, int isfamily) {
        this.localId = localId;
        this.id = id;
        this.searchText = searchText;
        this.owner = owner;
        this.server = server;
        this.ispublic = ispublic;
        this.isfriend = isfriend;
        this.farm = farm;
        this.secret = secret;
        this.title = title;
        this.isfamily = isfamily;
    }

    protected FlickerPhotosPhoto(Parcel in) {
        localId = in.readInt();
        id = in.readString();
        searchText = in.readString();
        owner = in.readString();
        server = in.readString();
        ispublic = in.readInt();
        isfriend = in.readInt();
        farm = in.readInt();
        secret = in.readString();
        title = in.readString();
        isfamily = in.readInt();
    }

    public static final Creator<FlickerPhotosPhoto> CREATOR = new Creator<FlickerPhotosPhoto>() {
        @Override
        public FlickerPhotosPhoto createFromParcel(Parcel in) {
            return new FlickerPhotosPhoto(in);
        }

        @Override
        public FlickerPhotosPhoto[] newArray(int size) {
            return new FlickerPhotosPhoto[size];
        }
    };

    public void setSearchText(@NonNull String searchText) {
        this.searchText = searchText;
    }

    public String getId() {
        return id;
    }

    @NonNull
    public String getSearchText() {
        return searchText;
    }

    @NonNull
    public String getOwner() {
        return owner;
    }

    @NonNull
    public String getServer() {
        return server;
    }

    public int getLocalId() {
        return localId;
    }

    public int getIspublic() {
        return ispublic;
    }

    public int getIsfriend() {
        return isfriend;
    }

    public int getFarm() {
        return farm;
    }

    @NonNull
    public String getImageId() {
        return id;
    }

    @NonNull
    public String getSecret() {
        return secret;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public int getIsfamily() {
        return isfamily;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setServer(@NonNull String server) {
        this.server = server;
    }

    public void setIspublic(int ispublic) {
        this.ispublic = ispublic;
    }

    public void setIsfriend(int isfriend) {
        this.isfriend = isfriend;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public void setSecret(@NonNull String secret) {
        this.secret = secret;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setIsfamily(int isfamily) {
        this.isfamily = isfamily;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(localId);
        dest.writeString(id);
        dest.writeString(searchText);
        dest.writeString(owner);
        dest.writeString(server);
        dest.writeInt(ispublic);
        dest.writeInt(isfriend);
        dest.writeInt(farm);
        dest.writeString(secret);
        dest.writeString(title);
        dest.writeInt(isfamily);
    }
}
