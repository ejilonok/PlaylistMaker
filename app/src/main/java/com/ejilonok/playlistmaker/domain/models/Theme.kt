package com.ejilonok.playlistmaker.domain.models

data class Theme (var currentTheme: THEMES = THEMES.LIGHT) {
    enum class THEMES{
        LIGHT,
        NIGHT
    }
}