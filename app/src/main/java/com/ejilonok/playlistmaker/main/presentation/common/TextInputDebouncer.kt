package com.ejilonok.playlistmaker.main.presentation.common

import android.os.Handler
import android.os.Looper

class TextInputDebouncer(
    private val runnable : Runnable,
    private val delay : Long) {

    fun execute() {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, delay)
    }

    fun stop() {
        handler.removeCallbacks(runnable)
    }

    fun onDestroy() {
        stop()
    }

    companion object {
        private val handler = Handler(Looper.getMainLooper())
    }
}