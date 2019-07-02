package com.ryanyu.easymedia

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import com.ryanyu.easymedia.RYEasyMedia.cleanFileEvent
import com.ryanyu.easymedia.listener.RYEasyMediaGetFileResult

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



class RYEasyMediaGetFile(val myActivity: Activity, val context: Context) {
    val ACTIVITY_RESULT_OPEN_FILE = 12
    private var imageUri: Uri? = null
    var fileType = "*/*"
    var muiltFileType:Array<String>? = null
    var multiple = false
    var localOnly = true
    var requestCode = 999
    var multipleMax = 99999
    var multipleMin = 0

    var ryEasyMediaGetFileResult: RYEasyMediaGetFileResult? = null

    private val FILE_PROVIDER_AUTHORITY = ".fileprovider"

    fun setRYEasyMediaGetFileResult(ryEasyMediaGetFileResult: RYEasyMediaGetFileResult?,requestCode : Int): RYEasyMediaGetFile? {
        this.ryEasyMediaGetFileResult = ryEasyMediaGetFileResult
        this.requestCode = requestCode
        return this
    }

    fun fileType(type: String): RYEasyMediaGetFile? {
        this.fileType = type
        return this
    }

    fun fileType(muiltFileType: Array<String>?): RYEasyMediaGetFile? {
        this.muiltFileType = muiltFileType
        return this
    }


    fun localOnly(localOnly: Boolean): RYEasyMediaGetFile? {
        this.localOnly = localOnly
        return this
    }

    fun multiple(multiple: Boolean): RYEasyMediaGetFile? {
        this.multiple = multiple
        return this
    }

    fun multipleMax(multipleMax: Int): RYEasyMediaGetFile? {
        this.multipleMax = multipleMax
        return this
    }

    fun multipleMin(multipleMin: Int): RYEasyMediaGetFile? {
        this.multipleMin = multipleMin
        return this
    }

    fun start() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        if(muiltFileType!=null){
            intent.type = "*/*";
            intent.putExtra(Intent.EXTRA_MIME_TYPES, muiltFileType)

        }else {
            intent.type = fileType
        }
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

    fun onActivityResultHandle(requestCode: Int?, resultCode: Int?, data: Intent?) {
        when (requestCode) {
            ACTIVITY_RESULT_OPEN_FILE -> if (resultCode == RESULT_OK) {
                data?.data?.let {
                    var abc = handleImageOnKitKat(data?.data)
                    if(abc.equals("")) {
                        ryEasyMediaGetFileResult?.onFileIsGet(null,this.requestCode)
                    }else{
                        ryEasyMediaGetFileResult?.onFileIsGet(Uri.parse(abc),this.requestCode)

                    }
                    cleanFileEvent()
                }

                data?.clipData?.let {
                    var max = false
                    var min = false
                    if(it.itemCount>multipleMax){
                        max = true
                    }
                    if(it.itemCount<multipleMin){
                        min = true
                    }

                    if(max || min){
                        ryEasyMediaGetFileResult?.onMaxMinOver(min,max,this.requestCode)
                        cleanFileEvent()
                        return
                    }

                    var uriArray :ArrayList<Uri?> = ArrayList()
                    for (i in 0 until it.itemCount){
                        uriArray.add(Uri.parse(handleImageOnKitKat(it.getItemAt(i).uri)))
                    }

                    ryEasyMediaGetFileResult?.onMultipleFileIsGet(uriArray,this.requestCode)

                }
            }
        }
    }

    private fun handleImageOnKitKat(uri: Uri) : String {
        var imagePath: String? = null

        if (DocumentsContract.isDocumentUri(context, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri!!.authority) {
                //Log.d(TAG, uri.toString());
                val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                return getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)!!
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val id = DocumentsContract.getDocumentId(uri)
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:".toRegex(), "")
                }
                var contentUri = uri
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                    )
                }

                return getDataColumn(context,contentUri)
            } else if("com.android.externalstorage.documents" == uri.authority){
                var path: String? = null
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    path = (Environment.getExternalStorageDirectory().toString() + "/" + split[1])
                    return path
                }
            }
        } else if ("content".equals(uri!!.scheme!!, ignoreCase = true)) {
            //Log.d(TAG, "content: " + uri.toString());
            return getImagePath(uri, null)!!
        }
        return ""
    }

    fun getDataColumn(context: Context, uri: Uri): String {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return ""
    }

    private fun getImagePath(uri: Uri?, selection: String?): String? {
        try {
            var path: String? = null
            val cursor = context.applicationContext?.contentResolver?.query(uri, null, selection, null, null)
            if (cursor != null) {
                if (cursor!!.moveToFirst()) {
                    path = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Images.Media.DATA))
                }

                cursor!!.close()
            }
            return path
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ""
        }
    }
}
