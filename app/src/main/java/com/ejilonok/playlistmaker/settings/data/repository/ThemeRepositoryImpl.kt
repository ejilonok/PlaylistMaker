package com.ejilonok.playlistmaker.settings.data.repository

import android.content.SharedPreferences
import android.content.res.Configuration
import com.ejilonok.playlistmaker.settings.data.dto.ThemeDto
import com.ejilonok.playlistmaker.settings.data.dto.ThemeMapper
import com.ejilonok.playlistmaker.settings.domain.api.ConfigurationProvider
import com.ejilonok.playlistmaker.settings.domain.api.repository.ThemeRepository
import com.ejilonok.playlistmaker.settings.domain.models.Theme

class ThemeRepositoryImpl(
    private val configurationProvider: ConfigurationProvider,
    private val sharedPreferences: SharedPreferences) : ThemeRepository {
    override fun getSavedThemeState(): Theme {
        val nightMode = configurationProvider.getConfiguration().uiMode and Configuration.UI_MODE_NIGHT_MASK
        val systemTheme = if (nightMode == Configuration.UI_MODE_NIGHT_YES) Theme.THEMES.NIGHT.ordinal else Theme.THEMES.LIGHT.ordinal
        val themeDto = try {
            ThemeDto(Theme.THEMES.entries[sharedPreferences.getInt(THEME_TAG, systemTheme)])
        } catch (e : Exception) {
            ThemeDto()
        }
        return ThemeMapper.map(themeDto)
    }

    override fun saveThemeState(theme: Theme) {
        val editor = sharedPreferences.edit()
        val themeDto = ThemeMapper.map(theme)
        editor.putInt(THEME_TAG, themeDto.currentTheme.ordinal)
        editor.apply()
    }

    companion object {
        const val SHARED_PREFERENCES_NAME = "com.ejilonok.playlistmaker.ThemeSettings"
        private const val THEME_TAG = "isDarkTheme"
    }
}