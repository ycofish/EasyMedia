package com.epiccomm.fsee.ryanlib.utils

import android.graphics.Bitmap
import android.net.Uri

/**
 * Created by Ryan Yu on 14/1/2019.
 */

interface RYEasyMediaTakePhotoResult {
    fun onBitmapIsReady(bitmap: Bitmap, coverBitmap: Bitmap, bitmapUri: Uri?)
}