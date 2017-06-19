package com.ahmedadelsaid.simplenetworklibrary.imagerequest;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.ahmedadelsaid.simplenetworklibrary.SimpleNetworkUtils;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * * ImageMemoryCache is the class that responsible for caching bitmaps.
 */

class ImageMemoryCache {

    private static ImageMemoryCache imageMemoryCache;
    private LruCache<String, Bitmap> imageLruCache;

    private ImageMemoryCache() {
        imageMemoryCache();
    }

    static ImageMemoryCache getInstance() {
        if (imageMemoryCache == null)
            imageMemoryCache = new ImageMemoryCache();
        return imageMemoryCache;
    }

    private void imageMemoryCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        imageLruCache = new LruCache<>(cacheSize);
    }

    void addImageToCache(String key, Bitmap bitmap) {
        if (getImageFromCache(key) == null) {
            imageLruCache.put(SimpleNetworkUtils.getCacheKey(key), bitmap);
        }
    }

    Bitmap getImageFromCache(String key) {
        return imageLruCache.get(SimpleNetworkUtils.getCacheKey(key));
    }

    void clearImageCache() {
        imageLruCache.evictAll();
    }

    void removeImage(String cacheKey) {
        imageLruCache.remove(SimpleNetworkUtils.getCacheKey(cacheKey));
    }

}
