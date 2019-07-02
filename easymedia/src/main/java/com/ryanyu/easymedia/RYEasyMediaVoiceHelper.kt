package com.ryanyu.easymedia

import android.content.Context
import android.os.Environment
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import com.ryanyu.easymedia.RYEasyMedia.cleanRecordVoiceEvent
import com.ryanyu.easymedia.listener.RYEasyMediaPlayVoiceResult
import com.ryanyu.easymedia.listener.RYEasyMediaRecordVoiceResult
import com.ryanyu.easymedia.listener.RYEasyMediaTakePhotoResult
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

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

class RYEasyMediaVoiceHelper {
    private var player: MediaPlayer? = null
    private var recorder: MediaRecorder? = null
    private var fileName: String = ""
    var ryEasyMediaRecordVoiceResult: RYEasyMediaRecordVoiceResult? = null
    var ryEasyMediaPlayVoiceResult: RYEasyMediaPlayVoiceResult? = null

    fun setRYEasyMediaRecordVoiceResult(ryEasyMediaRecordVoiceResult: RYEasyMediaRecordVoiceResult):RYEasyMediaVoiceHelper{
        this.ryEasyMediaRecordVoiceResult = ryEasyMediaRecordVoiceResult
        return this
    }

    fun setRYEasyMediaPlayVoiceResult(ryEasyMediaPlayVoiceResult: RYEasyMediaPlayVoiceResult):RYEasyMediaVoiceHelper{
        this.ryEasyMediaPlayVoiceResult = ryEasyMediaPlayVoiceResult
        return this
    }


    fun startRecording(): RYEasyMediaVoiceHelper {
        fileName = Environment.getExternalStorageDirectory().absolutePath
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        fileName += "/VOICE_$timeStamp.mp4"
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        }
        try {
            recorder?.prepare()
        } catch (e: IOException) {

        }

        recorder?.start()
        return this
    }

    fun getFileName(): String {
        return fileName
    }

    fun startPlaying() {
        try {
            player = MediaPlayer()
            player?.setDataSource(fileName)
            player?.prepare()
            player?.start()
            player?.setOnCompletionListener {
                ryEasyMediaPlayVoiceResult?.onCompletionListener()
            }
        } catch (e: Exception) {
            onStop()
        }

    }


    fun startPlaying(ctx: Context, filename: Uri) {
        try {
            player = MediaPlayer()
            player?.setDataSource(ctx, filename)
            player?.prepare()
            player?.start()
            player?.setOnCompletionListener {
                ryEasyMediaPlayVoiceResult?.onCompletionListener()
            }
        } catch (e: Exception) {
            onStop()
        }
    }


    fun stopPlaying() {
        player?.release()
        player = null
    }

    fun stopRecording() {
        try {
            recorder?.stop()
            recorder?.release()
            recorder = null
            ryEasyMediaRecordVoiceResult?.onVoiceRecorded(Uri.parse(fileName))
        } catch (e: Exception) {
            onStop()
        }
    }

    fun onStop() {
        recorder?.release()
        recorder = null
        player?.release()
        player = null
        cleanRecordVoiceEvent()
    }
}