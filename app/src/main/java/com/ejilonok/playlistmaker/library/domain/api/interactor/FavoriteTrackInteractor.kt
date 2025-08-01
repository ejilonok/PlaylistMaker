package com.ejilonok.playlistmaker.library.domain.api.interactor

import com.ejilonok.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    fun favoriteTracks() : Flow<List<Track>>
    fun isTrackFavorite(track: Track) : Flow<Boolean>
    suspend fun addTrackToFavorite(track: Track)

    suspend fun removeTrackFromFavorite(track: Track)
}