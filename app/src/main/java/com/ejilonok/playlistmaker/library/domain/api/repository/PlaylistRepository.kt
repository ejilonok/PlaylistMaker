package com.ejilonok.playlistmaker.library.domain.api.repository

import com.ejilonok.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getPlaylists() : Flow<List<Playlist>>
    fun isPlaylistExist(playlist: Playlist) : Flow<Boolean>
    suspend fun addPlaylist(playlist: Playlist)
}