package com.ahmedadelsaid.simplenetworklibrary.networkrequest;

import android.content.Context;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * NetworkRequest is the public class that the developer can use to make HTTP requests.
 */

public class NetworkRequest {

    private static NetworkRequest networkRequest;

    private Context context;
    private String baseUrl;
    private String endpoint;
    private boolean isDecodedUrl;
    private RequestType requestType;
    private ContentType contentType;
    private Map<String, String> params;
    private Map<String, String> headers;
    private JSONObject bodyJsonObject;
    private OnNetworkRequestResponseListener onNetworkRequestResponseListener;
    private final ResponseMemoryCache responseMemoryCache;
    private NetworkRequestAsyncTask networkRequestAsyncTask;

    private NetworkRequest(Context context) {
        this.context = context;
        this.responseMemoryCache = ResponseMemoryCache.getInstance();
    }

    public static NetworkRequest getInstance(Context context) {
        if (networkRequest == null)
            createInstance(context);
        return networkRequest;
    }

    private synchronized static void createInstance(Context context) {
        if (networkRequest == null)
            networkRequest = new NetworkRequest(context);
    }

    public NetworkRequest baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return getInstance(context);
    }

    public NetworkRequest endpoint(String endpoint) {
        this.endpoint = endpoint;
        return getInstance(context);
    }

    public NetworkRequest decodedUrl(boolean decodedUrl) {
        isDecodedUrl = decodedUrl;
        return getInstance(context);
    }

    public NetworkRequest requestType(RequestType requestType) {
        this.requestType = requestType;
        return getInstance(context);
    }

    public NetworkRequest contentType(ContentType contentType) {
        this.contentType = contentType;
        return getInstance(context);
    }

    public NetworkRequest params(Map<String, String> params) {
        this.params = params;
        return getInstance(context);
    }

    public NetworkRequest headers(Map<String, String> headers) {
        this.headers = headers;
        return getInstance(context);
    }

    public NetworkRequest bodyJsonObject(JSONObject bodyJsonObject) {
        this.bodyJsonObject = bodyJsonObject;
        return getInstance(context);
    }

    public NetworkRequest onNetworkRequestResponseListener(OnNetworkRequestResponseListener onNetworkRequestResponseListener) {
        this.onNetworkRequestResponseListener = onNetworkRequestResponseListener;
        return getInstance(context);
    }

    public void fireRequest() {
        networkRequestAsyncTask = new NetworkRequestAsyncTask(context, baseUrl, endpoint, requestType, contentType,
                params, headers, bodyJsonObject);
        networkRequestAsyncTask.setDecodedUrlInUTF(isDecodedUrl);
        networkRequestAsyncTask.setOnNetworkRequestResponseListener(onNetworkRequestResponseListener);
        networkRequestAsyncTask.execute();
    }

    public void clearResponseCache() {
        responseMemoryCache.clearResponseCache();
    }

    public void removeResponse(String cacheKey) {
        responseMemoryCache.removeResponse(cacheKey);
    }

    public void cancelRequest(boolean mayInterruptIfRunning) {
        if (networkRequestAsyncTask != null)
            networkRequestAsyncTask.cancel(mayInterruptIfRunning);
    }

}
