package com.ejilonok.playlistmaker.library.domain.impl

import com.ejilonok.playlistmaker.library.domain.api.interactor.FavoriteTrackInteractor
import com.ejilonok.playlistmaker.library.domain.api.repository.FavoriteTrackRepository
import com.ejilonok.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(
    private val favoriteTrackRepository: FavoriteTrackRepository
) : FavoriteTrackInteractor {
    override fun favoriteTracks(): Flow<List<Track>> {
        return favoriteTrackRepository.favoriteTracks()
    }

    override fun isTrackFavorite(track: Track): Flow<Boolean> {
        return favoriteTrackRepository.isTrackExist(track)
    }
    override suspend fun addTrackToFavorite(track: Track) {
        favoriteTrackRepository.addTrackToFavorite(track)
    }

    override suspend fun removeTrackFromFavorite(track: Track) {
        favoriteTrackRepository.removeFromFavoriteTrack(track)
    }
}