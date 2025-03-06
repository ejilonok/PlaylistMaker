package com.ejilonok.playlistmaker.domain.api.repository

import com.ejilonok.playlistmaker.domain.models.Theme

interface ThemeRepository {
    fun getSavedThemeState() : Theme

    fun saveThemeState(theme: Theme)
}