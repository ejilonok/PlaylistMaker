package com.ejilonok.playlistmaker.main.presentation

import androidx.lifecycle.ViewModel
import com.ejilonok.playlistmaker.main.domain.Navigator
import com.ejilonok.playlistmaker.main.presentation.common.ClickDebouncer
import com.ejilonok.playlistmaker.settings.domain.api.interactor.ThemeInteractor

class MainViewModel(
    private val themeInteractor : ThemeInteractor,
    private val navigator : Navigator,
    private val clickDebouncer : ClickDebouncer
) : ViewModel() {
    fun onCreate() {
        themeInteractor.setSavedTheme()
    }

    fun onClicked(action : MainActions) {
        if (clickDebouncer.can()) {
            when (action) {
                is MainActions.SearchClicked -> navigator.gotoSearch()
                is MainActions.LibraryClicked -> navigator.gotoLibrary()
                is MainActions.SettingsClicked -> navigator.gotoSettings()
            }
        }
    }

    fun onDestroy() {
        clickDebouncer.clearCalls()
    }
    companion object {
        const val CLICK_DEBOUNCE = 500L
    }
}