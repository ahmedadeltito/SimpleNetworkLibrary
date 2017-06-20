package com.ahmedadelsaid.simplenetworklibrary.networkrequest;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * OnNetworkRequestResponseListener is a observer that used to handle the response of network request calls.
 */

public interface OnNetworkRequestResponseListener {

    void onSuccessResponse(String response, ContentType contentType, boolean isCached);

    void onErrorResponse(String error, String message, int code);

}
