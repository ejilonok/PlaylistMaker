package com.ejilonok.playlistmaker.settings.data.dto

import com.ejilonok.playlistmaker.settings.domain.models.Theme

data class ThemeDto (var currentTheme: Theme.THEMES = Theme.THEMES.LIGHT) {
}