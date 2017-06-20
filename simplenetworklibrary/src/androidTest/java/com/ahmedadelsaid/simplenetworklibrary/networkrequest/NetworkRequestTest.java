package com.ahmedadelsaid.simplenetworklibrary.networkrequest;

import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Test;

/**
 * Created by Ahmed Adel on 20/06/2017.
 * <p>
 * NetworkRequestTest is a test case for fireRequest method in NetworkRequest class.
 */
public class NetworkRequestTest implements OnNetworkRequestResponseListener {

    @Test
    public void fireRequest() throws Exception {

        String BASE_URL = "https://jsonplaceholder.typicode.com";
        String END_POINT = Uri.parse("posts").toString();

        NetworkRequest networkRequest = NetworkRequest.getInstance(InstrumentationRegistry.getTargetContext())
                .baseUrl(BASE_URL)
                .endpoint(END_POINT)
                .requestType(RequestType.GET)
                .contentType(ContentType.JSON)
                .onNetworkRequestResponseListener(this);

        networkRequest.fireRequest();

    }

    @Override
    public void onSuccessResponse(String response, ContentType contentType, boolean isCached) {
        Log.d("NetworkRequestTest", response);
    }

    @Override
    public void onErrorResponse(String error, String message, int code) {
        Log.d("NetworkRequestTest", error);
    }
}