package com.ahmedadelsaid.simplenetworklibrary.imagerequest;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * OnImageBitmapListener is a parent interface class that is used in ImageSingleRequest class
 */

interface OnImageBitmapListener {
    Bitmap getBitmap(Context context);
}
