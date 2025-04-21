package com.ejilonok.playlistmaker.library.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ejilonok.playlistmaker.databinding.ActivityLibraryBinding
import com.ejilonok.playlistmaker.library.presentation.LibraryViewModel

class LibraryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLibraryBinding
    private lateinit var libraryModel : LibraryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        libraryModel = ViewModelProvider(this,
            LibraryViewModel.getViewModelFactory())[LibraryViewModel::class.java]

        libraryModel.closeActivityEventLiveData.observe(this) {
            finish()
        }

        binding.libraryBackButton.setOnClickListener {
            libraryModel.onBackClicked()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
