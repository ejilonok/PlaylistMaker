package com.ejilonok.playlistmaker.settings.presentation

sealed interface SettingsActions {
    data object ShareAppClicked : SettingsActions
    data object SupportClicked : SettingsActions
    data object TermsOfUseClicked : SettingsActions
    data object BackButtonClicked : SettingsActions
}