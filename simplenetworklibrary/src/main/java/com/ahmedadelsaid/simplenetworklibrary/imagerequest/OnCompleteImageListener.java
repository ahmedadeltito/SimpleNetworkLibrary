package com.ahmedadelsaid.simplenetworklibrary.imagerequest;

import android.graphics.Bitmap;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * OnCompleteImageListener is a listener that helps the developer to get the image bitmap if he/she makes an only image
 * single request not a multiple image requests as in the recycler view adapter as an example.
 */

public interface OnCompleteImageListener {

    void onComplete(Bitmap bitmap);
}
