package com.ejilonok.playlistmaker.library.presentation.playlists

import com.ejilonok.playlistmaker.library.domain.models.Playlist

sealed interface PlaylistMakerState {
    data object Empty : PlaylistMakerState
    data class CanSave(val playlist: Playlist) : PlaylistMakerState
    data class NoTitle(val playlist: Playlist) : PlaylistMakerState
}