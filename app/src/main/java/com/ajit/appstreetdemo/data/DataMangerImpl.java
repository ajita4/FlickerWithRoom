package com.ajit.appstreetdemo.data;

import android.util.Log;

import com.ajit.appstreetdemo.ApplicationController;
import com.ajit.appstreetdemo.Constants;
import com.ajit.appstreetdemo.data.models.Flicker;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataMangerImpl implements DataManger {


    private final Gson gson;
    DataEmitter dataEmitter;

    public DataMangerImpl(DataEmitter dataEmitter) {
        this.gson = new Gson();
        this.dataEmitter = dataEmitter;
    }

    @Override
    public boolean checkOffline(ImagesRequest imagesRequest) {
        return false;
    }

    @Override
    public void offlineData(ImagesRequest imagesRequest) {

    }

    @Override
    public void serverData(ImagesRequest imagesRequest) {
        /*https://api.flickr.com/services/rest/
        ?method=flickr.photos.search
        &api_key=66d101ac7c761a761ca47e688aa6556d&text=bagar+bihar
        &per_page=100
        &page=2&format=json&nojsoncallback=1
        &auth_token=72157680048828988-036203e7deb4fe93
        &api_sig=67f85f88af7afab8aaf095d70d6a6338*/
        /*https://api.flickr.com/services/rest/?method=flickr.groups.search&api_key=776aa6d326b1b0cf5b0dc7150b3943c0&text=nature&per_page=10&page=1&format=json&nojsoncallback=1*/
        StringBuilder stringBuilder = new StringBuilder(Constants.URL);
        stringBuilder.append("?method=" + "flickr.photos.search");
        stringBuilder.append("&api_key=" + Constants.API_KEY);
        stringBuilder.append("&text=" + imagesRequest.getSearchText());
        stringBuilder.append("&per_page=" + imagesRequest.getPerPage());
        stringBuilder.append("&page=" + imagesRequest.getPage());
        stringBuilder.append("&format=json&nojsoncallback=1");
        //stringBuilder.append("&auth_token=72157680048828988-036203e7deb4fe93");
        //stringBuilder.append("&api_sig=dfe2580c4f46a0979a24d648fc928fae");
        Log.e("url",  stringBuilder.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, stringBuilder.toString(), new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    Flicker flicker = gson.fromJson(response.toString(), Flicker.class);
                    if (flicker.getStat().equals("ok")) {
                        dataEmitter.setData(flicker);
                    }
                }

            }
        }, error -> {
            dataEmitter.error();
        }) {
          /*  @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;

            }*/

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest, Constants.TAG_API_IMAGELIST);
    }
}
