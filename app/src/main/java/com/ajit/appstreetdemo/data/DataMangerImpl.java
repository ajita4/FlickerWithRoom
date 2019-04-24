package com.ajit.appstreetdemo.data;

import android.util.Log;

import com.ajit.appstreetdemo.ApplicationController;
import com.ajit.appstreetdemo.Constants;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        String url = Constants.URL;
        Log.e(null, "url:" + url);
        ImagesRequest defautlRequest = new ImagesRequest();
        final Gson gson = new Gson();
        String jsreq = gson.toJson(defautlRequest);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsreq);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest, "postGCmId");

        return null;
    }
}
