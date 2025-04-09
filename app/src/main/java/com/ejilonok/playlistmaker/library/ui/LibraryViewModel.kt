package com.ejilonok.playlistmaker.library.ui

import android.app.Activity
import com.ejilonok.playlistmaker.databinding.ActivityLibraryBinding

class LibraryViewModel (
    private val activity: Activity,
    private val binding : ActivityLibraryBinding
) {
    fun onCreate() {
        binding.libraryBackButton.setOnClickListener {
            activity.finish()
        }
    }
    fun onDestroy() {}
}