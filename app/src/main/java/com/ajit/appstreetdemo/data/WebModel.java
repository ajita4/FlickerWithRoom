package com.ajit.appstreetdemo.data;

import java.util.List;

public class WebModel {

    private DataManger dataManger;

    public WebModel() {
        this.dataManger = new DataMangerImpl();
    }

    public List<ImageItem> getImageList(String searchItem) {
        if (dataManger.checkOffline(searchItem)) {
            return dataManger.getOfflineData(searchItem);
        } else {
            return dataManger.downloadFromServer(searchItem);
        }
    }
}
