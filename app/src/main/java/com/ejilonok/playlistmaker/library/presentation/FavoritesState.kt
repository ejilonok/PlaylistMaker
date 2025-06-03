package com.ejilonok.playlistmaker.library.presentation

sealed interface FavoritesState {
    data object Empty : FavoritesState
}
