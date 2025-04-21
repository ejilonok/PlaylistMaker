package com.ejilonok.playlistmaker.main.presentation

sealed interface MainActions {
    data object SearchClicked : MainActions
    data object LibraryClicked : MainActions
    data object SettingsClicked : MainActions
}