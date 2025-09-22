package com.ejilonok.playlistmaker.library.domain.api.interactor

import com.ejilonok.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun playlists() : Flow<List<Playlist>>
    fun isPlaylistExist(playlist: Playlist) : Flow<Boolean>
    suspend fun addPlaylist(playlist: Playlist)
}