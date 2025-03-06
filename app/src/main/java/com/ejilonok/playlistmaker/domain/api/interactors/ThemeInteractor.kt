package com.ejilonok.playlistmaker.domain.api.interactors

interface ThemeInteractor {
    fun setDarkTheme(isDark : Boolean)

    fun isSavedThemeDark() : Boolean
    fun setSavedTheme()
}