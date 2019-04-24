package com.ajit.appstreetdemo.data;

import com.google.gson.Gson;

import java.util.List;

public class DataMangerImpl implements DataManger {


    private final Gson gson;

    public DataMangerImpl() {
        this.gson = new Gson();
    }

    @Override
    public boolean checkOffline(String searchItem) {
        return false;
    }

    @Override
    public List<ImageItem> getOfflineData(String searchItem) {
        return null;
    }

    @Override
    public List<ImageItem> downloadFromServer(String searchItem) {
        return null;
    }
}
