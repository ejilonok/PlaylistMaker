package com.ejilonok.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ejilonok.playlistmaker.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding?.let {
            setContentView(it.root)

            resumeTheme()

            it.searchButton.setOnClickListener {
                val searchIntent = Intent(this, SearchActivity::class.java)
                startActivity(searchIntent)
            }

            it.libraryButton.setOnClickListener {
                val libraryIntent = Intent(this, LibraryActivity::class.java)
                startActivity(libraryIntent)
            }

            it.settingsButton.setOnClickListener {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
        }
    }
    private fun resumeTheme() {
        val isDarkTheme = ThemeManager.getSavedThemeState(this)
        ThemeManager.setDarkTheme(isDarkTheme)
    }
}