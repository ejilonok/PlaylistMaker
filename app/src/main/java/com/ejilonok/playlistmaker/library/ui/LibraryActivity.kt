package com.ejilonok.playlistmaker.library.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.databinding.ActivityLibraryBinding

class LibraryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLibraryBinding
    private lateinit var libraryModel : LibraryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        libraryModel = LibraryViewModel(this, binding)
        libraryModel.onCreate()
    }

    override fun onDestroy() {
        libraryModel.onDestroy()
        super.onDestroy()
    }
}
