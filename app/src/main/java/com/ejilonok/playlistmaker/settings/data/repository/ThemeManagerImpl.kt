package com.ejilonok.playlistmaker.settings.data.repository

import androidx.appcompat.app.AppCompatDelegate
import com.ejilonok.playlistmaker.settings.domain.api.repository.ThemeManager

class ThemeManagerImpl : ThemeManager {
    override fun setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

}