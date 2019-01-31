package com.epiccomm.fsee.ryanlib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Created by Ryan Yu on 2/1/2019.
 */

object RYEasyMedia {
    var ryEasyMediaTakePhoto: RYEasyMediaTakePhoto? = null
    var ryEasyMediaTakeVideo: RYEasyMediaTakeVideo? = null
    var ryEasyMediaGetFile: RYEasyMediaGetFile? = null

    val IMAGE = "image/*"
    val PDF = "application/pdf"
    val AUDIO = "audio/*"
    val VIDEO = "video/*"


    fun takePhoto(ctx: Context, activity: Activity): RYEasyMediaTakePhoto? {
        ryEasyMediaTakePhoto = RYEasyMediaTakePhoto(activity, ctx)
        return ryEasyMediaTakePhoto
    }

    fun takeVideo(ctx: Context, activity: Activity): RYEasyMediaTakeVideo? {
        ryEasyMediaTakeVideo = RYEasyMediaTakeVideo(activity, ctx)
        return ryEasyMediaTakeVideo
    }

    fun getFile(ctx: Context, activity: Activity): RYEasyMediaGetFile? {
        ryEasyMediaGetFile = RYEasyMediaGetFile(activity, ctx)
        return ryEasyMediaGetFile
    }

    fun onActivityResultHandle(requestCode: Int?, resultCode: Int?, data: Intent?) {
        ryEasyMediaTakePhoto?.OnActivityResultHandle(requestCode, resultCode, data)
        ryEasyMediaTakeVideo?.OnActivityResultHandle(requestCode, resultCode, data)
        ryEasyMediaGetFile?.OnActivityResultHandle(requestCode, resultCode, data)
    }

    fun cleanPhotoEvent() {
        ryEasyMediaTakePhoto = null
    }

    fun cleanVideoEvent() {
        ryEasyMediaTakeVideo = null
    }
}