package com.ejilonok.playlistmaker.library.data.repository

import com.ejilonok.playlistmaker.library.data.db.AppDatabase
import com.ejilonok.playlistmaker.library.data.db.FavoriteTrackEntity
import com.ejilonok.playlistmaker.library.data.dto.FavoriteTrackConverter
import com.ejilonok.playlistmaker.library.domain.api.repository.FavoriteTrackRepository
import com.ejilonok.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favoriteTrackConverter: FavoriteTrackConverter
) : FavoriteTrackRepository {
    override fun favoriteTracks(): Flow<List<Track>> =
        appDatabase.favoriteTrackDao().getFavoriteTracks()
        .map { favoriteTracks ->
            convertFromFavoriteTracksEntity(favoriteTracks)
        }
        .catch {
            emit(emptyList()) // В случае ошибки возвращаем пустой список
        }

    override fun isTrackExist(track: Track) : Flow<Boolean> =
        appDatabase.favoriteTrackDao().getTrackById(track.trackId)
            .map { it != null }
            .catch { emit(false) }

    private fun convertFromFavoriteTracksEntity(favoriteTracks: List<FavoriteTrackEntity>): List<Track> {
        return favoriteTracks.map { favoriteTrack -> favoriteTrackConverter.map(favoriteTrack) }
    }

    override suspend fun addTrackToFavorite(track: Track) {
        appDatabase.favoriteTrackDao().insertFavoriteTrack( favoriteTrackConverter.map(track) )
    }

    override suspend fun removeFromFavoriteTrack(track: Track) {
        appDatabase.favoriteTrackDao().removeFromFavorite( track.trackId )
    }
}