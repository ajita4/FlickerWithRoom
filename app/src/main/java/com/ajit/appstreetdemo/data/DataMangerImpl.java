package com.ajit.appstreetdemo.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ajit.appstreetdemo.ApplicationController;
import com.ajit.appstreetdemo.Constants;
import com.ajit.appstreetdemo.data.database.FlickerRoomDatabase;
import com.ajit.appstreetdemo.data.database.PhotoDao;
import com.ajit.appstreetdemo.data.models.Flicker;
import com.ajit.appstreetdemo.data.models.FlickerPhotos;
import com.ajit.appstreetdemo.data.models.FlickerPhotosPhoto;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import static com.ajit.appstreetdemo.Constants.API_STATUS_OK;

public class DataMangerImpl implements DataManger {


    private final Gson gson;
    DataEmitter dataEmitter;
    PhotoDao photoDao;


    public DataMangerImpl(DataEmitter dataEmitter) {
        this.gson = new Gson();
        this.dataEmitter = dataEmitter;
        FlickerRoomDatabase flickerRoomDatabase = FlickerRoomDatabase.getDatabase((Context) dataEmitter);
        photoDao = flickerRoomDatabase.flickerDao();
    }

    @Override
    public boolean checkOffline(ImagesRequest imagesRequest) {
        FlickerPhotos flickerPhotos = photoDao.getPhotos(imagesRequest.getSearchText());
        if (flickerPhotos != null) {
            return true;
        }
        return false;
    }

    @Override
    public void offlineData(ImagesRequest imagesRequest, String status) {
        FlickerPhotos flickerPhotos = photoDao.getPhotos(imagesRequest.getSearchText());
        if (flickerPhotos == null) {
            return;
        }
        flickerPhotos.setPhoto(photoDao.getPhotosPhotoList(imagesRequest.getPerPage(), imagesRequest.getLastId(), imagesRequest.getSearchText()));
        if (flickerPhotos.getPhoto().size() == 0) {
            return;
        }
        Flicker flicker = new Flicker(status, flickerPhotos);
        dataEmitter.setLastId(flickerPhotos.getPhoto().get(flickerPhotos.getPhoto().size() - 1).getLocalId());
        dataEmitter.setData(flicker);
    }

    @Override
    public void serverData(ImagesRequest imagesRequest) {
        StringBuilder stringBuilder = new StringBuilder(Constants.URL);
        stringBuilder.append("?method=" + "flickr.photos.search");
        stringBuilder.append("&api_key=" + Constants.API_KEY);
        stringBuilder.append("&text=" + imagesRequest.getSearchText());
        stringBuilder.append("&per_page=" + imagesRequest.getPerPage());
        stringBuilder.append("&page=" + imagesRequest.getPage());
        stringBuilder.append("&format=json&nojsoncallback=1");
        //stringBuilder.append("&auth_token=72157680048828988-036203e7deb4fe93");
        //stringBuilder.append("&api_sig=dfe2580c4f46a0979a24d648fc928fae");
        Log.e("url", stringBuilder.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, stringBuilder.toString(), new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    Flicker flicker = gson.fromJson(response.toString(), Flicker.class);
                    if (flicker.getStat().equals(API_STATUS_OK)) {
                        flicker.getPhotos().setSearchText(imagesRequest.getSearchText());
                        saveInDB(flicker, imagesRequest, imagesRequest.getSearchText());
                    }
                }

            }
        }, error -> {
            dataEmitter.error();
        });

        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest, Constants.TAG_API_IMAGELIST);
    }

    private void saveInDB(Flicker flicker, ImagesRequest imagesRequest, String searchText) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                // Photos pager
                if (photoDao.getPhotos(imagesRequest.getSearchText()) != null) {
                    // update
                    photoDao.updateFlickerPhotos(flicker.getPhotos());
                } else {
                    photoDao.insertFlickerPhotos(flicker.getPhotos());
                }

                // set searchText in new Data
                for (FlickerPhotosPhoto flickerPhotosPhoto : flicker.getPhotos().getPhoto()) {
                    flickerPhotosPhoto.setSearchText(searchText);
                }
                // Photos List insert update
                if (photoDao.getAllPhotosPhotoList(searchText).size() > 0) {
                    for (FlickerPhotosPhoto flickerPhotosPhoto : flicker.getPhotos().getPhoto()) {
                        if (photoDao.isPhotoAvaliable(flickerPhotosPhoto.getImageId(), searchText)) {
                            photoDao.updateAllFlickerPhotosPhoto(flickerPhotosPhoto);
                        } else {
                            photoDao.insertFlickerPhotosPhoto(flickerPhotosPhoto);
                        }
                    }

                } else {
                    photoDao.insertAllFlickerPhotosPhoto(flicker.getPhotos().getPhoto());
                }
                offlineData(imagesRequest, API_STATUS_OK);
                return null;
            }

        }.execute();

    }
}
