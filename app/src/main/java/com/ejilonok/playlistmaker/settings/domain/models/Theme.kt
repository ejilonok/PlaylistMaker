package com.ejilonok.playlistmaker.settings.domain.models

data class Theme (var currentTheme: THEMES = THEMES.LIGHT) {
    enum class THEMES{
        LIGHT,
        NIGHT
    }
}