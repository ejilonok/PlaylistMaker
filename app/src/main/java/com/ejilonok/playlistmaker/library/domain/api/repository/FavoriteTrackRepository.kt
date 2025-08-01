package com.ejilonok.playlistmaker.library.domain.api.repository

import com.ejilonok.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepository {
    fun favoriteTracks() : Flow<List<Track>>
    fun isTrackExist(track: Track) : Flow<Boolean>

    suspend fun addTrackToFavorite(track: Track)
    suspend fun removeFromFavoriteTrack(track: Track)
}