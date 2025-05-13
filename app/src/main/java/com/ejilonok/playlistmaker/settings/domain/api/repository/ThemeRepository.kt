package com.ejilonok.playlistmaker.settings.domain.api.repository

import com.ejilonok.playlistmaker.settings.domain.models.Theme

interface ThemeRepository {
    fun getSavedThemeState() : Theme

    fun saveThemeState(theme: Theme)
}