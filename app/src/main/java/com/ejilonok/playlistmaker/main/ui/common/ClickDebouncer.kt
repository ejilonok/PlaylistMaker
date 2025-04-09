package com.ejilonok.playlistmaker.main.ui.common

import android.os.Handler
import android.os.Looper

class ClickDebouncer(val debounceDelay : Long ) {
    private var isClickAllowed = true
    fun can(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(
                { isClickAllowed = true },
                DEBOUNCE_TOKEN,
                debounceDelay)
        }
        return current
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(DEBOUNCE_TOKEN)
    }

    companion object {
        private val handler = Handler(Looper.getMainLooper())
        private val DEBOUNCE_TOKEN = Any()
    }
}