package com.ahmedadelsaid.simplenetworkproject.controller;

/**
 * Created by Ahmed Adel on 19/06/2017.
 */

public interface OnResponseListener {

    void onSuccess(String response);

    void onError(String error, String message, int code);

}
