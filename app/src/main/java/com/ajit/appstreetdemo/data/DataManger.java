package com.ajit.appstreetdemo.data;

import com.ajit.appstreetdemo.data.models.FlickerPhotosPhoto;

import java.util.List;

public interface DataManger {
    boolean checkOffline(ImagesRequest imagesRequest);

    void offlineData(ImagesRequest imagesRequest,String status);

    void serverData(ImagesRequest imagesRequest);
}
