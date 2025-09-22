package com.ejilonok.playlistmaker.library.presentation.playlists

sealed interface PlaylistMakerAction {
    data object GoBack :
        PlaylistMakerAction
    data object SelectCover :
        PlaylistMakerAction
}