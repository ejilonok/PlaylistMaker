package com.ejilonok.playlistmaker.library.domain.impl

import com.ejilonok.playlistmaker.library.domain.api.interactor.PlaylistInteractor
import com.ejilonok.playlistmaker.library.domain.api.repository.PlaylistRepository
import com.ejilonok.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl (
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override fun playlists() : Flow<List<Playlist>> =
        playlistRepository.getPlaylists()
    override fun isPlaylistExist(playlist: Playlist) : Flow<Boolean> =
        playlistRepository.isPlaylistExist(playlist)

    override suspend fun addPlaylist(playlist: Playlist) =
        playlistRepository.addPlaylist(playlist)
}