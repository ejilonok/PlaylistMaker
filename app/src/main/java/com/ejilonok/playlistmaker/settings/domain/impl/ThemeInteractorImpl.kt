package com.ejilonok.playlistmaker.settings.domain.impl

import com.ejilonok.playlistmaker.settings.domain.api.interactor.ThemeInteractor
import com.ejilonok.playlistmaker.settings.domain.api.repository.ThemeManager
import com.ejilonok.playlistmaker.settings.domain.api.repository.ThemeRepository
import com.ejilonok.playlistmaker.settings.domain.models.Theme

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