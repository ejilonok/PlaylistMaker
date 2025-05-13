package com.ejilonok.playlistmaker.main.presentation.common

import android.os.Handler
import android.os.Looper

class ClickDebouncer(
    private val debounceDelay : Long ) {
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

    fun clearCalls() {
        handler.removeCallbacksAndMessages(DEBOUNCE_TOKEN)
    }

    companion object {
        private val handler = Handler(Looper.getMainLooper())
        private val DEBOUNCE_TOKEN = Any()
    }
}