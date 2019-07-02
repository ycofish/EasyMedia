package com.ryanyu.easymedia

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.support.v4.content.FileProvider
import com.ryanyu.easymedia.RYEasyMedia
import com.ryanyu.easymedia.listener.RYEasyMediaTakeVideoResult
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.io.FileOutputStream

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

class RYEasyMediaTakeVideo(val myActivity: Activity, val context: Context) {
    val ACTIVITY_RESULT_TAKE_VIDEO = 11
    private var videoUri: Uri? = null
    private var covervideoUri: Uri? = null
    private var realVideoUri: Uri? = null
    private var videoFile: File? = null
    private var videoFileName: String? = null
    private var saveAsRoot: Boolean? = null
    var requestCode = 999
    var ryEasyMediaTakeVideoResult: RYEasyMediaTakeVideoResult? = null
    private val FILE_PROVIDER_AUTHORITY = ".fileprovider"
    private var imageName: String? = null
    private var videoSec: Int? = null
    private var videoSize: Int? = null
    private var isHighQuality: Boolean? = true

    fun setRyEasyMediaTakeVideoResult(ryEasyMediaTakeVideoResult: RYEasyMediaTakeVideoResult? ,requestCode : Int): RYEasyMediaTakeVideo? {
        this.ryEasyMediaTakeVideoResult = ryEasyMediaTakeVideoResult
        this.requestCode = requestCode
        return this
    }

    fun saveAsRoot(saveAsRoot: Boolean?): RYEasyMediaTakeVideo? {
        this.saveAsRoot = saveAsRoot
        return this
    }

    fun sec(videoSec:Int) : RYEasyMediaTakeVideo{
        this.videoSec = videoSec
        return this
    }

    fun size(mb:Int) : RYEasyMediaTakeVideo{
        this.videoSize = mb*1048*1048
        return this
    }

    fun isHighQuality(isHighQuality:Boolean):RYEasyMediaTakeVideo{
        this.isHighQuality = isHighQuality
        return this
    }



    fun start() {
        videoFile = null
        videoUri = null
        var intent = Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE)
        videoFile = createvideoFile(saveAsRoot!!)

        realVideoUri = Uri.parse(videoFile?.absoluteFile?.getPath())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            videoUri = FileProvider.getUriForFile(
                myActivity!!,
                myActivity!!.packageName + FILE_PROVIDER_AUTHORITY,
                videoFile!!
            )
        } else {
            videoUri = Uri.fromFile(videoFile)
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)


        if(videoSize!=null){
            intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, videoSize!!)
        }

        if(isHighQuality!=null){
            if(isHighQuality as Boolean)
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
            else
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)
        }

        if(videoSec!=null){
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, videoSec!!)
        }


        myActivity?.startActivityForResult(intent, ACTIVITY_RESULT_TAKE_VIDEO)
    }

    private fun createvideoFile(isSaveToRoot: Boolean): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        videoFileName = "MP4_" + timeStamp + "_"
        var storageDir: File?
        if (isSaveToRoot) {
            storageDir = myActivity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        } else {
            storageDir = Environment.getExternalStorageDirectory()
        }
        var videoFile: File? = null
        try {
            videoFile = File.createTempFile(videoFileName, ".mp4", storageDir)

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return videoFile
    }

    fun onActivityResultHandle(requestCode: Int?, resultCode: Int?, data: Intent?) {
        when (requestCode) {
            ACTIVITY_RESULT_TAKE_VIDEO -> if (resultCode == RESULT_OK) {
                realVideoUri?.let { ryEasyMediaTakeVideoResult?.onVideoIsReady(realVideoUri!!,this.requestCode) }
                RYEasyMedia.cleanVideoEvent()
            }
        }
    }

}