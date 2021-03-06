package com.ryanyu.easymedia

import android.app.Activity
import android.content.Context
import android.content.Intent

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

object RYEasyMedia {
    var ryEasyMediaTakePhoto: RYEasyMediaTakePhoto? = null
    var ryEasyMediaTakeVideo: RYEasyMediaTakeVideo? = null
    var ryEasyMediaVoiceHelper: RYEasyMediaVoiceHelper? = null
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

    fun voiceHelper(): RYEasyMediaVoiceHelper? {
        ryEasyMediaVoiceHelper = RYEasyMediaVoiceHelper()
        return ryEasyMediaVoiceHelper
    }

    fun onActivityResultHandle(requestCode: Int?, resultCode: Int?, data: Intent?) {
        ryEasyMediaTakePhoto?.onActivityResultHandle(requestCode, resultCode, data)
        ryEasyMediaTakeVideo?.onActivityResultHandle(requestCode, resultCode, data)
        ryEasyMediaGetFile?.onActivityResultHandle(requestCode, resultCode, data)
    }

    fun cleanPhotoEvent() {
        ryEasyMediaTakePhoto = null
    }

    fun cleanVideoEvent() {
        ryEasyMediaTakeVideo = null
    }

    fun cleanFileEvent() {
        ryEasyMediaGetFile = null
    }

    fun cleanRecordVoiceEvent() {
        ryEasyMediaVoiceHelper = null
    }
}