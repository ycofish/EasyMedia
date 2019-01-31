package com.epiccomm.fsee.ryanlib.utils

import android.graphics.Bitmap
import android.net.Uri

/**
 * Created by Ryan Yu on 14/1/2019.
 */

interface RYEasyMediaTakeVideoResult {
    fun onVideoIsReady(uri: Uri)
}