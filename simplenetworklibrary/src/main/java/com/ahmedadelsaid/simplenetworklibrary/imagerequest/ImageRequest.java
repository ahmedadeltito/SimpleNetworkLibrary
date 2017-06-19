package com.ahmedadelsaid.simplenetworklibrary.imagerequest;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * * ImageRequest is the public class that the developer can use to make image requests.
 */

public class ImageRequest {

    private static ImageRequest imageRequest;
    private final Context context;
    private final int LOADING_THREADS = 4;
    private ExecutorService threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
    private ImageTask currentImageTask;

    private final ImageMemoryCache imageMemoryCache;

    private ImageRequest(Context context) {
        this.context = context;
        imageMemoryCache = ImageMemoryCache.getInstance();
    }

    public static ImageRequest getInstance(Context context) {
        if (imageRequest == null)
            imageRequest = new ImageRequest(context);
        return imageRequest;
    }

    public void loadImage(final String imageUrl, final ImageView imageView, final Integer fallbackResource,
                            final Integer loadingResource, final OnCompleteImageListener onCompleteImageListener) {
        if (loadingResource != null) {
            imageView.setImageResource(loadingResource);
        }
        if (currentImageTask != null) {
            currentImageTask.cancel();
            currentImageTask = null;
        }
        currentImageTask = new ImageTask(context, new ImageSingleRequest(imageUrl));
        currentImageTask.setOnImageCompleteHandler(new ImageTask.OnImageCompleteHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    if (fallbackResource != null) {
                        imageView.setImageResource(fallbackResource);
                    }
                }
                if (onCompleteImageListener != null) {
                    onCompleteImageListener.onComplete(bitmap);
                }
            }
        });
        threadPool.execute(currentImageTask);
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
