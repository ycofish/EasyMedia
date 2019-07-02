package com.ryanyu.easymedia.listener

import android.graphics.Bitmap
import android.net.Uri

/**
 * Update 2019-02-01
 *
 * ██████╗ ██╗   ██╗ █████╗ ███╗   ██╗    ██╗   ██╗██╗   ██╗    ██╗     ██╗██████╗ ██████╗  █████╗ ██████╗ ██╗   ██╗
 * ██╔══██╗╚██╗ ██╔╝██╔══██╗████╗  ██║    ╚██╗ ██╔╝██║   ██║    ██║     ██║██╔══██╗██╔══██╗██╔══██╗██╔══██╗╚██╗ ██╔╝
 * ██████╔╝ ╚████╔╝ ███████║██╔██╗ ██║     ╚████╔╝ ██║   ██║    ██║     ██║██████╔╝██████╔╝███████║██████╔╝ ╚████╔╝
 * ██╔══██╗  ╚██╔╝  ██╔══██║██║╚██╗██║      ╚██╔╝  ██║   ██║    ██║     ██║██╔══██╗██╔══██╗██╔══██║██╔══██╗  ╚██╔╝
 * ██║  ██║   ██║   ██║  ██║██║ ╚████║       ██║   ╚██████╔╝    ███████╗██║██████╔╝██║  ██║██║  ██║██║  ██║   ██║
 * ╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═══╝       ╚═╝    ╚═════╝     ╚══════╝╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝
 *
 *
 * _|_|_|_|                                    _|      _|                    _|   _|
 * _|           _|_|_|     _|_|_|   _|    _|   _|_|  _|_|     _|_|       _|_|_|          _|_|_|
 * _|_|_|     _|    _|   _|_|       _|    _|   _|  _|  _|   _|_|_|_|   _|    _|   _|   _|    _|
 * _|         _|    _|       _|_|   _|    _|   _|      _|   _|         _|    _|   _|   _|    _|
 * _|_|_|_|     _|_|_|   _|_|_|       _|_|_|   _|      _|     _|_|_|     _|_|_|   _|     _|_|_|
 *
 *
 * Created by Ryan Yu.
 */

interface RYEasyMediaTakePhotoResult {
    fun onBitmapIsReady(bitmap: Bitmap, coverBitmap: Bitmap, bitmapUri: Uri?, requestCode: Int)
    fun onMultipleBitmapIsReady( coverBitmap: ArrayList<Bitmap>, bitmapUri: ArrayList<Uri?>, requestCode: Int)
    fun onGetBitmapFail(requestCode: Int)
}