package com.ajit.appstreetdemo.util;

import android.content.Context;
import android.util.DisplayMetrics;

import com.ajit.appstreetdemo.ApplicationController;
import com.ajit.appstreetdemo.data.models.FlickerPhotosPhoto;

public class Utility {

    public static String getImageUrlFromIds(FlickerPhotosPhoto imageItem, ImageSize imageSize) {

        /*https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg*/
        StringBuilder stringBuilder = new StringBuilder("https://farm");
        stringBuilder.append(imageItem.getFarm());
        stringBuilder.append(".staticflickr.com/");
        stringBuilder.append(imageItem.getServer() + "/");
        stringBuilder.append(imageItem.getId() + "_");
        stringBuilder.append(imageItem.getSecret() + "_");
        stringBuilder.append(imageSize.getName() + ".jpg");
        return stringBuilder.toString();
    }

    public int deviceWidthInDp(Context context) {
        return (int) (deviceWidthInPx(context) / deviceDensity());
    }

    public int deviceWidthInPx(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    private DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public float deviceDensity() {
        return ApplicationController.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
    }


}
