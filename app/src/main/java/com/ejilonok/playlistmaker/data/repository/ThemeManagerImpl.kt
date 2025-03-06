package com.ejilonok.playlistmaker.data.repository

import androidx.appcompat.app.AppCompatDelegate
import com.ejilonok.playlistmaker.domain.api.repository.ThemeManager

class ThemeManagerImpl : ThemeManager {
    override fun setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

}