package com.example.dictionaryappakash

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


object AppHelperFunction {

    fun isConnectedToInternet(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        /*version check*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val state = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (state != null) {
                when {
                    state.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    state.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    state.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeInfo = connectivityManager.activeNetworkInfo
            if (activeInfo != null && activeInfo.isConnected) {
                return true
            }
        }
        return false
    }


    /* play audio */
    fun playAudio(audioUri:String){

        val mediaplayer  = MediaPlayer()
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaplayer.setDataSource(audioUri)
            mediaplayer.prepare()
            mediaplayer.start()
        }catch (e : Exception){

        }
    }
}