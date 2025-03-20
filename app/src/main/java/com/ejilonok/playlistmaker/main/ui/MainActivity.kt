package com.ejilonok.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.databinding.ActivityMainBinding
import com.ejilonok.playlistmaker.main.ui.common.ClickDebouncer

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val themeInteractor = Creator.provideThemeInteractor(this)
    private val clickDebouncer = ClickDebouncer(500L)
    private val navigator = Creator.provideNavigator(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        themeInteractor.setSavedTheme()

        binding.searchButton.setOnClickListener {
            if (clickDebouncer.can()) {
                navigator.gotoSearch()
            } }
        binding.libraryButton.setOnClickListener {
            if (clickDebouncer.can()) {
                navigator.gotoLibrary()
            } }
        binding.settingsButton.setOnClickListener {
            if (clickDebouncer.can()) {
                navigator.gotoSettings()
            } }
    }
}