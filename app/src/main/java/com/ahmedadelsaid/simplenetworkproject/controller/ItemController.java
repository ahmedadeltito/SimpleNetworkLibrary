package com.ahmedadelsaid.simplenetworkproject.controller;

import android.content.Context;

import com.ahmedadelsaid.simplenetworklibrary.networkrequest.ContentType;
import com.ahmedadelsaid.simplenetworklibrary.networkrequest.OnNetworkRequestResponseListener;

/**
 * Created by Ahmed Adel on 19/06/2017.
 */

public class ItemController implements OnNetworkRequestResponseListener {

    public ItemController(Context context){

    }

    @Override
    public void onSuccessResponse(String response, ContentType contentType, boolean isCached) {

    }

    @Override
    public void onErrorResponse(String error, String message, int code) {

    }
}
