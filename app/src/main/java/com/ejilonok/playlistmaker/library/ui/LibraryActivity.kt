package com.ejilonok.playlistmaker.library.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.databinding.ActivityLibraryBinding
import com.ejilonok.playlistmaker.library.presentation.LibraryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLibraryBinding
    private val libraryModel : LibraryViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        libraryModel.closeActivityEventLiveData.observe(this) {
            finish()
        }

        binding.libraryBackButton.setOnClickListener {
            libraryModel.onBackClicked()
        }
    }
}
