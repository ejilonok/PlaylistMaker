package com.ejilonok.playlistmaker.main.presentation.common

import android.os.Handler

class TextInputDebouncer(
    private val mainHandler : Handler,
    private val delay : Long) {

    var runnable : Runnable? = null
        set(value) {
            stop()
            field = value
        }

    fun execute() {
        runnable?.let {
            mainHandler.removeCallbacks(it)
            mainHandler.postDelayed(it, delay)
        }
    }

    fun stop() {
        runnable?.let { mainHandler.removeCallbacks(it) }
    }

    fun onDestroy() {
        stop()
    }
}