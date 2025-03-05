package com.ejilonok.playlistmaker.data.dto

import com.ejilonok.playlistmaker.domain.models.Theme

data class ThemeDto (var currentTheme: Theme.THEMES = Theme.THEMES.LIGHT) {
}