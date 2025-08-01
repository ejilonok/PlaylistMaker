package com.ejilonok.playlistmaker.library.presentation

import com.ejilonok.playlistmaker.search.domain.models.Track

sealed interface FavoritesState {
    data object Empty : FavoritesState
    data object Loading : FavoritesState
    data class Content(val tracks : List<Track>) : FavoritesState
}
