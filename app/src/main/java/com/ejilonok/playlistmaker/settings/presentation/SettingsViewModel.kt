package com.ejilonok.playlistmaker.settings.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.main.presentation.common.ClickDebouncer
import com.ejilonok.playlistmaker.main.presentation.common.SingleLiveEvent

class SettingsViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val clickDebouncer = ClickDebouncer(CLICK_DEBOUNCE_DELAY)

    private val actualThemeIsDark = MutableLiveData(false)
    val actualThemeIsDarkLiveData : LiveData<Boolean> = actualThemeIsDark
    fun setThemeDark(isDark : Boolean) {
        actualThemeIsDark.postValue(isDark)
        themeInteractor.setDarkTheme(isDark)
    }

    private var themeInteractor = Creator.provideThemeInteractor(getApplication())
    private var sharingInteractor = Creator.provideSharingInteractor(getApplication())

    init {
        setThemeDark(themeInteractor.isSavedThemeDark())
    }
    fun onAction(action : SettingsActions) {
        if (clickDebouncer.can()) {
            when (action) {
                is SettingsActions.BackButtonClicked -> closeActivityEvent.postValue(Unit)
                is SettingsActions.ShareAppClicked -> sharingInteractor.shareApp()
                is SettingsActions.SupportClicked -> sharingInteractor.openSupport()
                is SettingsActions.TermsOfUseClicked -> sharingInteractor.openTerms()
            }
        }
    }

    private val closeActivityEvent = SingleLiveEvent(Unit)
    val closeActivityEventLiveData : LiveData<Unit> = closeActivityEvent

    override fun onCleared() {
        clickDebouncer.clearCalls()
        super.onCleared()
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 600L
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingsViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
                }
            }
    }
}