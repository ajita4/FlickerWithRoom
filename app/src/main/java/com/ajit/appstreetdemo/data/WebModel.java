package com.ajit.appstreetdemo.data;

import android.os.AsyncTask;

import com.ajit.appstreetdemo.ApplicationController;
import com.ajit.appstreetdemo.Constants;
import com.ajit.appstreetdemo.util.Utility;

public class WebModel {

    private DataManger dataManger;

    public WebModel(DataEmitter dataEmitter) {
        this.dataManger = new DataMangerImpl(dataEmitter);

    }

    public void imageList(ImagesRequest imagesRequest) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                if (!Utility.isConnected() && dataManger.checkOffline(imagesRequest)) {
                    dataManger.offlineData(imagesRequest, Constants.API_STATUS_OK);
                } else {
                    dataManger.serverData(imagesRequest);
                }
                return null;
            }
        }.execute();

    }

    public void closeRequest() {
        ApplicationController.getInstance().cancelPendingRequests(Constants.TAG_API_IMAGELIST);
    }
}
