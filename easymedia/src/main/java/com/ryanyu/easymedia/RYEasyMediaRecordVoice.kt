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
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.util.Log
import com.ryanyu.easymedia.RYEasyMedia
import com.ryanyu.easymedia.listener.RYEasyMediaTakePhotoResult
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

class RYEasyMediaRecordVoice(private val myActivity: Activity, val context: Context) {
    private var player: MediaPlayer? = null
    private var recorder: MediaRecorder? = null
    private var fileName: String = ""

     fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {

            }

            start()
        }
    }

     fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }


    fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

     fun onStop() {
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }
}