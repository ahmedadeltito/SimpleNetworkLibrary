package com.ahmedadelsaid.simplenetworkproject.controller;

import android.content.Context;
import android.net.Uri;

import com.ahmedadelsaid.simplenetworklibrary.networkrequest.ContentType;
import com.ahmedadelsaid.simplenetworklibrary.networkrequest.NetworkRequest;
import com.ahmedadelsaid.simplenetworklibrary.networkrequest.OnNetworkRequestResponseListener;
import com.ahmedadelsaid.simplenetworklibrary.networkrequest.RequestType;
import com.ahmedadelsaid.simplenetworkproject.model.ItemList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * ItemController is the controller/service class that handle the HTTP request to pastebin website and handling
 * the returned json.
 */

public class ItemController implements OnNetworkRequestResponseListener {

    private final String BASE_URL = "http://pastebin.com/raw";
    private final String END_POINT = Uri.parse("wgkJgazE").toString();
    private final NetworkRequest networkRequest;
    private OnResponseListener onResponseListener;

    public ItemController(Context context, OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
        networkRequest = NetworkRequest.getInstance(context)
                .baseUrl(BASE_URL)
                .endpoint(END_POINT)
                .requestType(RequestType.GET)
                .contentType(ContentType.JSON)
                .onNetworkRequestResponseListener(this);
    }

    public void execute() {
        networkRequest.fireRequest();
    }

    public void cancel(boolean mayInterruptIfRunning) {
        networkRequest.cancelRequest(mayInterruptIfRunning);
    }

    @Override
    public void onSuccessResponse(String response, ContentType contentType, boolean isCached) {
        if (contentType == ContentType.JSON) {
            try {
                JSONArray menuItemJsonArray = new JSONArray(response);
                Type menuItemListType = new TypeToken<ArrayList<ItemList>>() {
                }.getType();
                Gson gson = new Gson();
                ArrayList<ItemList> itemLists = gson.fromJson(menuItemJsonArray.toString(), menuItemListType);
                if (onResponseListener != null)
                    onResponseListener.onSuccess(itemLists);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorResponse(String error, String message, int code) {
        if (onResponseListener != null)
            onResponseListener.onError(error, message, code);
    }
}
