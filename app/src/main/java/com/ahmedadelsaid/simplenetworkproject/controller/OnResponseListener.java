package com.ahmedadelsaid.simplenetworkproject.controller;

import com.ahmedadelsaid.simplenetworkproject.model.ItemList;

import java.util.ArrayList;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * OnResponseListener is the listener that responsible for handling the response and the error of the requested API.
 */

public interface OnResponseListener {

    void onSuccess(ArrayList<ItemList> itemLists);

    void onError(String error, String message, int code);

}
