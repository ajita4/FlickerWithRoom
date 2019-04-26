package com.ajit.appstreetdemo.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "flicker_photos_table")
public class FlickerPhotos {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "search_text")
    private String searchText;
    @NonNull
    @ColumnInfo(name = "perpage")
    private int perpage;
    @NonNull
    @ColumnInfo(name = "total")
    private String total;
    @NonNull
    @ColumnInfo(name = "pages")
    private int pages;
    @Ignore
    private List<FlickerPhotosPhoto> photo;
    @NonNull
    @ColumnInfo(name = "page")
    private int page;

    public void setSearchText(@NonNull String searchText) {
        this.searchText = searchText;
    }

    public int getPerpage() {
        return perpage;
    }

    @NonNull
    public String getSearchText() {
        return searchText;
    }

    @NonNull
    public String getTotal() {
        return total;
    }

    public int getPages() {
        return pages;
    }

    @NonNull
    public List<FlickerPhotosPhoto> getPhoto() {
        return photo;
    }

    public void setPhoto(List<FlickerPhotosPhoto> photo) {
        this.photo = photo;
    }

    public int getPage() {
        return page;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public void setTotal(@NonNull String total) {
        this.total = total;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
