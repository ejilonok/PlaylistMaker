package com.ejilonok.playlistmaker.main.ui.common

import android.os.Handler
import android.os.Looper

class TextInputDebouncer(val runnable : Runnable, val delay : Long) {

    fun execute() {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, delay)
    }

    fun stop() {
        handler.removeCallbacks(runnable)
    }

    companion object {
        private val handler = Handler(Looper.getMainLooper())
    }
}