package com.ejilonok.playlistmaker.main.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.main.presentation.common.ClickDebouncer

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val clickDebouncer = ClickDebouncer(CLICK_DEBOUNCE)
    private val themeInteractor = Creator.provideThemeInteractor(getApplication())
    private val navigator = Creator.provideNavigator(getApplication())

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
        private const val CLICK_DEBOUNCE = 500L
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MainViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
                }
            }
    }
}