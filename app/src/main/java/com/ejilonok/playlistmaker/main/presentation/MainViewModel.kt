package com.ejilonok.playlistmaker.main.presentation

import androidx.lifecycle.ViewModel
import com.ejilonok.playlistmaker.settings.domain.api.interactor.ThemeInteractor

class MainViewModel(
    private val themeInteractor : ThemeInteractor
) : ViewModel() {
    fun onCreate() {
        themeInteractor.setSavedTheme()
    }
}
