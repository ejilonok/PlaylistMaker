package com.ejilonok.playlistmaker.ui.common

import android.os.Handler
import android.os.Looper

class ClickDebouncer(val debounceDelay : Long ) {
    private var isClickAllowed = true
    fun can(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, debounceDelay)
        }
        return current
    }

    companion object {
        private val handler = Handler(Looper.getMainLooper())
    }
}