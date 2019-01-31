package com.epiccomm.fsee.ryanlib.utils

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import android.database.Cursor
import android.annotation.SuppressLint
import java.io.File
import android.content.ContentResolver
import android.support.v4.content.CursorLoader


/**
 * Created by Ryan Yu on 2/1/2019.
 */

class RYEasyMediaGetFile(val myActivity: Activity, val context: Context) {
    val ACTIVITY_RESULT_OPEN_FILE = 12
    private var imageUri: Uri? = null
    var fileType = "*/*"
    var multiple = false
    var localOnly = true

    var ryEasyMediaGetFileResult: RYEasyMediaGetFileResult? = null

    private val FILE_PROVIDER_AUTHORITY = ".fileprovider"

    fun setRYEasyMediaGetFileResult(ryEasyMediaGetFileResult: RYEasyMediaGetFileResult?): RYEasyMediaGetFile? {
        this.ryEasyMediaGetFileResult = ryEasyMediaGetFileResult
        return this
    }

    fun fileType(type: String) {
        this.fileType = type
    }

    fun localOnly(localOnly: Boolean) {
        this.localOnly = localOnly
    }

    fun multiple(multiple: Boolean) {
        this.multiple = multiple
    }

    fun start() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = fileType
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, localOnly)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            myActivity.startActivityForResult(intent, ACTIVITY_RESULT_OPEN_FILE)
        } catch (ex: android.content.ActivityNotFoundException) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(
                myActivity, "Please install a File Manager.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun OnActivityResultHandle(requestCode: Int?, resultCode: Int?, data: Intent?) {
        when (requestCode) {
            ACTIVITY_RESULT_OPEN_FILE -> if (resultCode == RESULT_OK) {
                data?.data?.let {
                    var abc = handleImageOnKitKat(data)
                    Toast.makeText(context,abc,Toast.LENGTH_SHORT).show()
                    //   ryEasyMediaGetFileResult?.onFileIsGet(data.data)
                }
            }
        }
    }

    private fun handleImageOnKitKat(data: Intent) : String {
        var imagePath: String? = null
        val uri = data.data

        if (DocumentsContract.isDocumentUri(context, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri!!.authority) {
                //Log.d(TAG, uri.toString());
                val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                return getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)!!
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                //Log.d(TAG, uri.toString());
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(docId)
                )
                return getImagePath(contentUri, null)!!
            }
        } else if ("content".equals(uri!!.scheme!!, ignoreCase = true)) {
            //Log.d(TAG, "content: " + uri.toString());
           return getImagePath(uri, null)!!
        }
        return ""
    }

    private fun getImagePath(uri: Uri, selection: String?): String? {
        var path: String? = null
        val cursor = context.applicationContext?.contentResolver?.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor!!.moveToFirst()) {
                path = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Images.Media.DATA))
            }

            cursor!!.close()
        }
        return path
    }
}
