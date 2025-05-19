package com.ejilonok.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.databinding.ActivityMainBinding
import com.ejilonok.playlistmaker.main.presentation.MainActions
import com.ejilonok.playlistmaker.main.presentation.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val mainViewModel : MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.onCreate()

        binding.searchButton.setOnClickListener {
            mainViewModel.onClicked(MainActions.SearchClicked)
        }
        binding.libraryButton.setOnClickListener {
            mainViewModel.onClicked(MainActions.LibraryClicked)
        }
        binding.settingsButton.setOnClickListener {
            mainViewModel.onClicked(MainActions.SettingsClicked)
        }
    }

    override fun onDestroy() {
        mainViewModel.onDestroy()
        super.onDestroy()
    }
}