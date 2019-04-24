package com.ajit.appstreetdemo.data;

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
}
