package com.ajit.appstreetdemo.data;

import com.ajit.appstreetdemo.ApplicationController;
import com.ajit.appstreetdemo.Constants;

public class WebModel {

    private DataManger dataManger;

    public WebModel(DataEmitter dataEmitter) {
        this.dataManger = new DataMangerImpl(dataEmitter);
    }

    public void imageList(ImagesRequest imagesRequest) {
        if (dataManger.checkOffline(imagesRequest)) {
            dataManger.offlineData(imagesRequest);
        } else {
            dataManger.serverData(imagesRequest);
        }
    }

    public void closeRequest() {
        ApplicationController.getInstance().cancelPendingRequests(Constants.TAG_API_IMAGELIST);
    }
}
