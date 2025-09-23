package com.ejilonok.playlistmaker.library.presentation.playlists

import com.ejilonok.playlistmaker.library.domain.models.Playlist

sealed interface PlaylistListState {
    data object Empty : PlaylistListState
    data object Loading : PlaylistListState
    data class Content(val lists: List<Playlist>) : PlaylistListState
}