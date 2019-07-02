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
import com.ryanyu.easymedia.listener.RYEasyMediaTakePhotoResult
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.io.FileOutputStream
import kotlin.collections.ArrayList

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

class RYEasyMediaTakePhoto(private val myActivity: Activity, val context: Context) {
    val ACTIVITY_RESULT_TAKE_PHOTO = 10

    private var imageUri: Uri? = null
    private var realImageUri: Uri? = null
    private var coverImageUri: Uri? = null

    private var imageFile: File? = null
    private var coverImageFile: File? = null

    private var imageFileName: String? = null
    private var coverImageFileName: String? = null
    private var saveAsRoot: Boolean? = null
    private var isMultiple: Boolean = false
    private var multipleMax: Int = 9999
    var requestCode = 999


    var ryEasyMediaTakPhotoResult: RYEasyMediaTakePhotoResult? = null


    private val FILE_PROVIDER_AUTHORITY = ".fileprovider"


    private var imageName: String? = null

    fun setRYEasyMediaTakPhotoResult(
        ryEasyMediaTakPhotoResult: RYEasyMediaTakePhotoResult?,
        requestCode: Int
    ): RYEasyMediaTakePhoto? {
        this.ryEasyMediaTakPhotoResult = ryEasyMediaTakPhotoResult
        this.requestCode = requestCode
        return this
    }

    fun saveAsRoot(saveAsRoot: Boolean?): RYEasyMediaTakePhoto? {
        this.saveAsRoot = saveAsRoot
        return this
    }

    fun isMultiple(isMultiple: Boolean): RYEasyMediaTakePhoto? {
        this.isMultiple = isMultiple
        return this
    }

    fun start() {
        if (saveAsRoot == null) return
        imageFile = null
        imageUri = null
        var intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        imageFile = createImageFile(saveAsRoot!!)

        realImageUri = Uri.parse(imageFile?.absoluteFile?.getPath())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(
                myActivity!!,
                myActivity!!.packageName + FILE_PROVIDER_AUTHORITY,
                imageFile!!
            )
            coverImageUri = FileProvider.getUriForFile(
                myActivity!!,
                myActivity!!.packageName + FILE_PROVIDER_AUTHORITY,
                coverImageFile!!
            )

        } else {
            imageUri = Uri.fromFile(imageFile)
            coverImageUri = Uri.fromFile(coverImageFile)
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        myActivity?.startActivityForResult(intent, ACTIVITY_RESULT_TAKE_PHOTO)
    }

    private fun createImageFile(isSaveToRoot: Boolean): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        imageFileName = "JPEG_" + timeStamp + "_"
        var storageDir: File?
        if (isSaveToRoot) {
            storageDir = myActivity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        } else {
            storageDir = Environment.getExternalStorageDirectory()
        }
        var imageFile: File? = null
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
            coverImageFile = File.createTempFile("cover_$imageFileName", ".jpg", storageDir)

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return imageFile
    }

    fun onActivityResultHandle(requestCode: Int?, resultCode: Int?, data: Intent?) {
        when (requestCode) {
            ACTIVITY_RESULT_TAKE_PHOTO -> {
                if (resultCode == RESULT_OK) {
                    if (realImageUri != null) {
                        getImage(realImageUri)
                    }
                }else if (resultCode == Activity.RESULT_CANCELED) {
                    if (isMultiple) {
                        if(coverBitmapArray.size != 0){
                            ryEasyMediaTakPhotoResult?.onMultipleBitmapIsReady( coverBitmapArray, imageUriArray, this.requestCode)
                            RYEasyMedia.cleanPhotoEvent()
                        }else{
                            ryEasyMediaTakPhotoResult?.onGetBitmapFail(this.requestCode)
                            RYEasyMedia.cleanPhotoEvent()
                        }
                    }else {
                        ryEasyMediaTakPhotoResult?.onGetBitmapFail(this.requestCode)
                        RYEasyMedia.cleanPhotoEvent()
                    }
                }
            }
        }
    }

    private fun getImage(imageUri: Uri?) {
        var bitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeFile(imageUri?.toString())
            //       bitmap = BitmapFactory.decodeStream(context?.getContentResolver()?.openInputStream(imageUri))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        if (bitmap == null) {
            val handler = Handler()
            handler.postDelayed({ getImage(imageUri) }, 500)
            return
        }

        var coverBitmap =
            Bitmap.createScaledBitmap(bitmap, (bitmap.width * 0.1).toInt(), (bitmap.height * 0.1).toInt(), true)
        saveCoverImage(coverBitmap)

        if (isMultiple) {
    //        bitmapArray.add(bitmap)
            coverBitmapArray.add(coverBitmap)
            imageUriArray.add(imageUri)
            if (coverBitmapArray.size == multipleMax) {
                ryEasyMediaTakPhotoResult?.onMultipleBitmapIsReady( coverBitmapArray, imageUriArray, requestCode)
                RYEasyMedia.cleanPhotoEvent()
            }else{
                start()
            }
        } else {
            ryEasyMediaTakPhotoResult?.onBitmapIsReady(bitmap, coverBitmap, imageUri, requestCode)
            RYEasyMedia.cleanPhotoEvent()
        }
    }

    var coverBitmapArray: ArrayList<Bitmap> = ArrayList()
    var imageUriArray: ArrayList<Uri?> = ArrayList()

    private fun saveCoverImage(finalBitmap: Bitmap) {
        try {
            val out = FileOutputStream(coverImageFile)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
            out.flush()
            out.close()
            //      galleryAddPic(Uri.fromFile(file));

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun galleryAddPic(uri: Uri) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        mediaScanIntent.data = uri
        context?.sendBroadcast(mediaScanIntent)
    }

}