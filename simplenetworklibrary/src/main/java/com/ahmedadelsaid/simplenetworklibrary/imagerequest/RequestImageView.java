package com.ahmedadelsaid.simplenetworklibrary.imagerequest;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Ahmed Adel on 19/06/2017.
 *
 * RequestImageView is an AppCompatImageView class that makes the requesting process of images more easier by calling
 * setImageUrl with passing on it the image url and some parameters to load the required image.
 *
 */

public class RequestImageView extends AppCompatImageView {
    private static final int LOADING_THREADS = 4;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
    private ImageTask currentTask;
    private ImageMemoryCache imageMemoryCache;


    public RequestImageView(Context context) {
        super(context);
        imageMemoryCache = ImageMemoryCache.getInstance();
    }

    public RequestImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        imageMemoryCache = ImageMemoryCache.getInstance();
    }

    public RequestImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        imageMemoryCache = ImageMemoryCache.getInstance();
    }

    public void setImageUrl(String url) {
        setImage(new ImageSingleRequest(url));
    }

    public void setImageUrl(String url, OnCompleteImageListener onCompleteImageListener) {
        setImage(new ImageSingleRequest(url), onCompleteImageListener);
    }

    public void setImageUrl(String url, final Integer fallbackResource) {
        setImage(new ImageSingleRequest(url), fallbackResource);
    }

    public void setImageUrl(String url, final Integer fallbackResource, OnCompleteImageListener onCompleteImageListener) {
        setImage(new ImageSingleRequest(url), fallbackResource, onCompleteImageListener);
    }

    public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new ImageSingleRequest(url), fallbackResource, loadingResource);
    }

    public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource, OnCompleteImageListener onCompleteImageListener) {
        setImage(new ImageSingleRequest(url), fallbackResource, loadingResource, onCompleteImageListener);
    }

    public void setImage(final OnImageBitmapListener image) {
        setImage(image, null, null, null);
    }

    public void setImage(final OnImageBitmapListener image, final OnCompleteImageListener onCompleteImageListener) {
        setImage(image, null, null, onCompleteImageListener);
    }

    public void setImage(final OnImageBitmapListener image, final Integer fallbackResource) {
        setImage(image, fallbackResource, fallbackResource, null);
    }

    public void setImage(final OnImageBitmapListener image, final Integer fallbackResource, OnCompleteImageListener onCompleteImageListener) {
        setImage(image, fallbackResource, fallbackResource, onCompleteImageListener);
    }

    public void setImage(final OnImageBitmapListener image, final Integer fallbackResource, final Integer loadingResource) {
        setImage(image, fallbackResource, loadingResource, null);
    }

    public void setImage(final OnImageBitmapListener image, final Integer fallbackResource, final Integer loadingResource, final OnCompleteImageListener onCompleteImageListener) {
        if (loadingResource != null) {
            setImageResource(loadingResource);
        }
        if (currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }
        currentTask = new ImageTask(getContext(), image);
        currentTask.setOnImageCompleteHandler(new ImageTask.OnImageCompleteHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if (bitmap != null) {
                    setImageBitmap(bitmap);
                } else {
                    if (fallbackResource != null) {
                        setImageResource(fallbackResource);
                    }
                }
                if (onCompleteImageListener != null) {
                    onCompleteImageListener.onComplete(bitmap);
                }
            }
        });
        threadPool.execute(currentTask);
    }

    public void clearImageCache() {
        imageMemoryCache.clearImageCache();
    }

    void removeImage(String cacheKey) {
        imageMemoryCache.removeImage(cacheKey);
    }

    public void cancelAllTasks() {
        threadPool.shutdownNow();
        threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
    }
}
