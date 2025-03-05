package com.ejilonok.playlistmaker.domain.impl

import com.ejilonok.playlistmaker.domain.api.interactors.ThemeInteractor
import com.ejilonok.playlistmaker.domain.api.repository.ThemeManager
import com.ejilonok.playlistmaker.domain.api.repository.ThemeRepository
import com.ejilonok.playlistmaker.domain.models.Theme

class ThemeInteractorImpl(
    private val themeRepository: ThemeRepository,
    private val themeManager: ThemeManager
) : ThemeInteractor {
    private var theme: Theme = Theme()
    override fun setDarkTheme(isDark: Boolean) {
        if (isDark)
            setDarkTheme()
        else
            setLightTheme()

        saveThemeState()
    }

    override fun isSavedThemeDark(): Boolean {
        theme.currentTheme = themeRepository.getSavedThemeState().currentTheme
        return theme.currentTheme == Theme.THEMES.NIGHT
    }

    override fun setSavedTheme() {
        setDarkTheme(isSavedThemeDark())
    }

    private fun saveThemeState() {
        themeRepository.saveThemeState(theme)
    }

    private fun setLightTheme() {
        themeManager.setLightTheme()
        theme.currentTheme = Theme.THEMES.LIGHT
    }

    private fun setDarkTheme() {
        themeManager.setDarkTheme()
        theme.currentTheme = Theme.THEMES.NIGHT
    }

}