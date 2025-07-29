package com.ejilonok.playlistmaker.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejilonok.playlistmaker.main.presentation.common.debounce
import com.ejilonok.playlistmaker.settings.domain.api.interactor.ThemeInteractor
import com.ejilonok.playlistmaker.sharing.domain.api.interactor.SharingInteractor

class SettingsViewModel(
    private val themeInteractor : ThemeInteractor,
    private val sharingInteractor : SharingInteractor
) : ViewModel() {
    val actionDebounce: (SettingsActions) -> Unit

    private val actualThemeIsDark = MutableLiveData(false)
    val actualThemeIsDarkLiveData : LiveData<Boolean> = actualThemeIsDark
    fun setThemeDark(isDark : Boolean) {
        if (isDark != actualThemeIsDark.value) {
            actualThemeIsDark.postValue(isDark)
            themeInteractor.setDarkTheme(isDark)
        }
    }

    init {
        setThemeDark(themeInteractor.isSavedThemeDark())

        actionDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewModelScope, false) { action ->
            onAction(action)
        }
    }
    private fun onAction(action : SettingsActions) {
        when (action) {
            is SettingsActions.ShareAppClicked -> sharingInteractor.shareApp()
            is SettingsActions.SupportClicked -> sharingInteractor.openSupport()
            is SettingsActions.TermsOfUseClicked -> sharingInteractor.openTerms()
        }
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 150L
    }
}
