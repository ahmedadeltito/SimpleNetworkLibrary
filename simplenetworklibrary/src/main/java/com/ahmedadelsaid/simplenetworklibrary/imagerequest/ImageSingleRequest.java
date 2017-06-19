package com.ahmedadelsaid.simplenetworklibrary.imagerequest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * ImageSingleRequest is a class that responsible for making single request to get image bitmap
 */

class ImageSingleRequest implements OnImageBitmapListener {

    private static final int CONNECT_TIMEOUT = 50000;
    private static final int READ_TIMEOUT = 10000;

    private String imageUrl;

    ImageSingleRequest(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private Bitmap getBitmapFromUrl(String url) {
        Bitmap bitmap = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            bitmap = BitmapFactory.decodeStream((InputStream) conn.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public Bitmap getBitmap(Context context) {
        ImageMemoryCache imageMemoryCache = ImageMemoryCache.getInstance();
        Bitmap bitmap = null;
        if (imageUrl != null) {
            bitmap = imageMemoryCache.getImageFromCache(imageUrl);
            if (bitmap == null) {
                bitmap = getBitmapFromUrl(imageUrl);
                if (bitmap != null) {
                    imageMemoryCache.addImageToCache(imageUrl, bitmap);
                }
            }
        }
        return bitmap;
    }
}
