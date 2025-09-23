package com.ejilonok.playlistmaker.library.presentation.playlists

sealed interface PlaylistListAction {
    data object CreateNewPlaylist : PlaylistListAction
}