package com.ejilonok.playlistmaker.main.ui.common

import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebouncingTextWatcher(
    private val scope: CoroutineScope,
    private val delay : Long = 300,
    private val onTextChanged: (String) -> Unit
) : TextWatcher {
    private var lastText = ""
    private var debounedJob : Job? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (lastText != (s?.toString() ?: "")) {
            lastText = s?.toString() ?: ""
            debounedJob?.cancel()
            debounedJob = scope.launch {
                delay( delay )
                onTextChanged(lastText)
            }
        }
    }
}