package com.ahmedadelsaid.simplenetworklibrary.networkrequest;

import android.content.Context;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * NetworkRequest is the public class that the developer can use to make HTTP requests
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

    public static NetworkRequest getInstance(NetworkRequestBuilder networkRequestBuilder) {
        if (networkRequest == null)
            networkRequest = new NetworkRequest(networkRequestBuilder);
        return networkRequest;
    }

    private NetworkRequest(NetworkRequestBuilder networkRequestBuilder) {
        this.context = networkRequestBuilder.context;
        this.baseUrl = networkRequestBuilder.baseUrl;
        this.endpoint = networkRequestBuilder.endpoint;
        this.isDecodedUrl = networkRequestBuilder.isDecodedUrl;
        this.requestType = networkRequestBuilder.requestType;
        this.contentType = networkRequestBuilder.contentType;
        this.params = networkRequestBuilder.params;
        this.headers = networkRequestBuilder.headers;
        this.bodyJsonObject = networkRequestBuilder.bodyJsonObject;
        this.onNetworkRequestResponseListener = networkRequestBuilder.onNetworkRequestResponseListener;
        this.responseMemoryCache = ResponseMemoryCache.getInstance();
    }

    private void fireRequest() {
        NetworkRequestAsyncTask connection = new NetworkRequestAsyncTask(context, baseUrl, endpoint, requestType, contentType, params, headers, bodyJsonObject);
        connection.setDecodedUrlInUTF(isDecodedUrl);
        connection.setOnNetworkRequestResponseListener(onNetworkRequestResponseListener);
        connection.execute();
    }

    public void clearResponseCache() {
        responseMemoryCache.clearResponseCache();
    }

    public void removeResponse(String cacheKey) {
        responseMemoryCache.removeResponse(cacheKey);
    }

    public static class NetworkRequestBuilder {

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

        public NetworkRequestBuilder(Context context) {
            this.context = context;
        }

        public NetworkRequestBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public NetworkRequestBuilder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public NetworkRequestBuilder isDecodedUrl(boolean isDecodedUrl) {
            this.isDecodedUrl = isDecodedUrl;
            return this;
        }

        public NetworkRequestBuilder requestType(RequestType requestType) {
            this.requestType = requestType;
            return this;
        }

        public NetworkRequestBuilder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public NetworkRequestBuilder params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public NetworkRequestBuilder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public NetworkRequestBuilder bodyJsonObject(JSONObject bodyJsonObject) {
            this.bodyJsonObject = bodyJsonObject;
            return this;
        }

        public NetworkRequestBuilder onNetworkRequestResponseListener(OnNetworkRequestResponseListener onNetworkRequestResponseListener) {
            this.onNetworkRequestResponseListener = onNetworkRequestResponseListener;
            return this;
        }

        public NetworkRequest buildNetworkRequest() {
            return NetworkRequest.getInstance(this);
        }

    }

}
