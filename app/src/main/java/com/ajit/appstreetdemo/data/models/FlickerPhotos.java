package com.ajit.appstreetdemo.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FlickerPhotos implements Parcelable {
    public static final Creator<FlickerPhotos> CREATOR = new Creator<FlickerPhotos>() {
        @Override
        public FlickerPhotos createFromParcel(Parcel source) {
            FlickerPhotos var = new FlickerPhotos();
            var.perpage = source.readInt();
            var.total = source.readString();
            var.pages = source.readInt();
            var.photo = source.createTypedArrayList(FlickerPhotosPhoto.CREATOR);
            var.page = source.readInt();
            return var;
        }

        @Override
        public FlickerPhotos[] newArray(int size) {
            return new FlickerPhotos[size];
        }
    };
    private int perpage;
    private String total;
    private int pages;
    private List<FlickerPhotosPhoto> photo;
    private int page;

    public int getPerpage() {
        return this.perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return this.total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getPages() {
        return this.pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<FlickerPhotosPhoto> getPhoto() {
        return this.photo;
    }

    public void setPhoto(List<FlickerPhotosPhoto> photo) {
        this.photo = photo;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.perpage);
        dest.writeString(this.total);
        dest.writeInt(this.pages);
        dest.writeTypedList(this.photo);
        dest.writeInt(this.page);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
