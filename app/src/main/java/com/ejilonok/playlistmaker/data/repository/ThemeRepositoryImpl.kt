package com.ejilonok.playlistmaker.data.repository

import android.content.Context
import android.content.res.Configuration
import com.ejilonok.playlistmaker.data.dto.ThemeDto
import com.ejilonok.playlistmaker.data.dto.ThemeMapper
import com.ejilonok.playlistmaker.domain.api.repository.ThemeRepository
import com.ejilonok.playlistmaker.domain.models.Theme

class ThemeRepositoryImpl(private val context: Context) : ThemeRepository {
    override fun getSavedThemeState(): Theme {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val nightMode = context.applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val systemTheme = if (nightMode == Configuration.UI_MODE_NIGHT_YES) Theme.THEMES.NIGHT.ordinal else Theme.THEMES.LIGHT.ordinal
        val themeDto = try {
            ThemeDto(Theme.THEMES.entries[sharedPreferences.getInt(THEME_TAG, systemTheme)])
        } catch (e : Exception) {
            ThemeDto()
        }
        return ThemeMapper.map(themeDto)
    }

    override fun saveThemeState(theme: Theme) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val themeDto = ThemeMapper.map(theme)
        editor.putInt(THEME_TAG, themeDto.currentTheme.ordinal)
        editor.apply()
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "com.ejilonok.playlistmaker.ThemeSettings"
        private const val THEME_TAG = "isDarkTheme"
    }
}