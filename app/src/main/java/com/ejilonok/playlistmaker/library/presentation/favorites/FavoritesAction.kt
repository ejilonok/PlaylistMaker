package com.ejilonok.playlistmaker.library.presentation.favorites

import com.ejilonok.playlistmaker.search.domain.models.Track

sealed interface FavoritesAction {
    data class GotoPlayerAction(val track: Track) : FavoritesAction
}