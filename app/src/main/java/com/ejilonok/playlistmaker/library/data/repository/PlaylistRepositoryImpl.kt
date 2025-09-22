package com.ejilonok.playlistmaker.library.data.repository

import com.ejilonok.playlistmaker.library.data.db.AppDatabase
import com.ejilonok.playlistmaker.library.data.db.entity.PlaylistEntity
import com.ejilonok.playlistmaker.library.data.dto.PlaylistConverter
import com.ejilonok.playlistmaker.library.domain.api.repository.PlaylistRepository
import com.ejilonok.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistConverter : PlaylistConverter
) : PlaylistRepository {
    override fun getPlaylists(): Flow<List<Playlist>> =
        appDatabase.playlistDao().getPlaylists().map { convert(it) }

    override fun isPlaylistExist(playlist: Playlist): Flow<Boolean> =
        appDatabase.playlistDao().getPlaylistByName(playlist.title)
            .map { it != null }
            .catch { false }

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist( playlistConverter.map(playlist) )
    }

    private fun convert (playlist: List<PlaylistEntity>) : List<Playlist> {
        return playlist.map { playlistConverter.map(it) }
    }
}