package com.ahmedadelsaid.simplenetworklibrary.networkrequest;

import android.util.LruCache;

import com.ahmedadelsaid.simplenetworklibrary.SimpleNetworkUtils;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * ResponseMemoryCache is the class that responsible for caching network library response.
 */

class ResponseMemoryCache {

    private static ResponseMemoryCache responseMemoryCache;
    private LruCache<String, String> responseLruCache;

    private ResponseMemoryCache() {
        responseMemoryCache();
    }

    static ResponseMemoryCache getInstance() {
        if (responseMemoryCache == null)
            responseMemoryCache = new ResponseMemoryCache();
        return responseMemoryCache;
    }

    private void responseMemoryCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        responseLruCache = new LruCache<>(cacheSize);
    }

    void addResponseToCache(String key, String response) {
        if (getResponseFromCache(key) == null) {
            responseLruCache.put(SimpleNetworkUtils.getCacheKey(key), response);
        }
    }

    String getResponseFromCache(String key) {
        return responseLruCache.get(SimpleNetworkUtils.getCacheKey(key));
    }

    void clearResponseCache() {
        responseLruCache.evictAll();
    }

    void removeResponse(String cacheKey) {
        responseLruCache.remove(SimpleNetworkUtils.getCacheKey(cacheKey));
    }
}
