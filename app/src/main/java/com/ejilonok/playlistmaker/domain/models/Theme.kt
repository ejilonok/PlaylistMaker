package com.ejilonok.playlistmaker.domain.models

class Theme (var currentTheme: THEMES = THEMES.LIGHT) {
    enum class THEMES{
        LIGHT,
        NIGHT
    }
}