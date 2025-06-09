package com.ejilonok.playlistmaker.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ejilonok.playlistmaker.main.presentation.common.ClickDebouncer
import com.ejilonok.playlistmaker.settings.domain.api.interactor.ThemeInteractor
import com.ejilonok.playlistmaker.sharing.domain.api.interactor.SharingInteractor

class SettingsViewModel(
    private val themeInteractor : ThemeInteractor,
    private val sharingInteractor : SharingInteractor,
    private val clickDebouncer : ClickDebouncer
) : ViewModel() {

    private val actualThemeIsDark = MutableLiveData(false)
    val actualThemeIsDarkLiveData : LiveData<Boolean> = actualThemeIsDark
    fun setThemeDark(isDark : Boolean) {
        actualThemeIsDark.postValue(isDark)
        themeInteractor.setDarkTheme(isDark)
    }

    init {
        setThemeDark(themeInteractor.isSavedThemeDark())
    }
    fun onAction(action : SettingsActions) {
        if (clickDebouncer.can()) {
            when (action) {
                is SettingsActions.ShareAppClicked -> sharingInteractor.shareApp()
                is SettingsActions.SupportClicked -> sharingInteractor.openSupport()
                is SettingsActions.TermsOfUseClicked -> sharingInteractor.openTerms()
            }
        }
    }

    override fun onCleared() {
        clickDebouncer.clearCalls()
        super.onCleared()
    }
    companion object {
        const val CLICK_DEBOUNCE_DELAY = 600L
    }
}
