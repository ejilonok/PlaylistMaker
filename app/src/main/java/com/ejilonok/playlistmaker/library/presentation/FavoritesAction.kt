package com.ejilonok.playlistmaker.library.presentation

import com.ejilonok.playlistmaker.search.domain.models.Track

interface FavoritesAction {
    data class GotoPlayerAction(val track: Track) : FavoritesAction
}