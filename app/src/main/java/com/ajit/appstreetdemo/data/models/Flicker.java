package com.ajit.appstreetdemo.data.models;

public class Flicker {

    private String stat;
    private FlickerPhotos photos;

    public Flicker(String stat, FlickerPhotos photos) {
        this.stat = stat;
        this.photos = photos;
    }

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

}
