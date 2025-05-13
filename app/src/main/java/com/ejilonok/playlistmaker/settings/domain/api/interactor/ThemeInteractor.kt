package com.ejilonok.playlistmaker.settings.domain.api.interactor

interface ThemeInteractor {
    fun setDarkTheme(isDark : Boolean)

    fun isSavedThemeDark() : Boolean
    fun setSavedTheme()
}