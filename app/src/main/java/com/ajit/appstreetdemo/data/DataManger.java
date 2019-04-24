package com.ajit.appstreetdemo.data;

import java.util.List;

public interface DataManger {
    boolean checkOffline(String searchItem);

    List<ImageItem> getOfflineData(String searchItem);

    List<ImageItem> downloadFromServer(String searchItem);
}
