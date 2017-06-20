package com.ahmedadelsaid.simplenetworklibrary.imagerequest;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * ImageTask is a thread safe structure to load and get the image bitmap in the background.
 */

class ImageTask implements Runnable {

    private static final int BITMAP_READY = 0;

    private Context context;

    private boolean cancelled = false;

    private OnImageCompleteHandler onImageCompleteHandler;
    private OnImageBitmapListener onImageBitmapListener;


    ImageTask(Context context, OnImageBitmapListener onImageBitmapListener) {
        this.onImageBitmapListener = onImageBitmapListener;
        this.context = context;
    }

    @Override
    public void run() {
        if (onImageBitmapListener != null) {
            complete(onImageBitmapListener.getBitmap(context));
            context = null;
        }
    }

    void setOnImageCompleteHandler(OnImageCompleteHandler onImageCompleteHandler) {
        this.onImageCompleteHandler = onImageCompleteHandler;
    }

    void cancel() {
        cancelled = true;
    }

    private void complete(Bitmap bitmap) {
        if (onImageCompleteHandler != null && !cancelled) {
            onImageCompleteHandler.sendMessage(onImageCompleteHandler.obtainMessage(BITMAP_READY, bitmap));
        }
    }

    static class OnImageCompleteHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            onComplete(bitmap);
        }

        void onComplete(Bitmap bitmap) {

        }
    }
}