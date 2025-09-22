package com.ejilonok.playlistmaker.library.data.dto

import com.ejilonok.playlistmaker.library.data.db.entity.PlaylistEntity
import com.ejilonok.playlistmaker.library.domain.models.Playlist

class PlaylistConverter {
    fun map(playlist: Playlist) : PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.title,
            playlist.description,
            playlist.coverFilename
        )
    }

    fun map(playlist: PlaylistEntity) : Playlist {
        return Playlist(
            playlist.playlistId,
            playlist.title,
            playlist.description,
            playlist.coverFilename
        )
    }
}