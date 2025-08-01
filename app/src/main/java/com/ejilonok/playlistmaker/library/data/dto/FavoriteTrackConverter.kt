package com.ejilonok.playlistmaker.library.data.dto

import com.ejilonok.playlistmaker.library.data.db.FavoriteTrackEntity
import com.ejilonok.playlistmaker.search.domain.models.Track

class FavoriteTrackConverter {

    fun map(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl)
    }

    fun map(favoriteTrack : FavoriteTrackEntity) : Track {
        return Track( trackId = favoriteTrack.trackId,
            trackName = favoriteTrack.trackName,
            artistName = favoriteTrack.artistName,
            trackTime = favoriteTrack.trackTime,
            artworkUrl100 = favoriteTrack.artworkUrl100,
            collectionName = favoriteTrack.collectionName,
            releaseDate = favoriteTrack.releaseDate,
            primaryGenreName = favoriteTrack.primaryGenreName,
            country = favoriteTrack.country,
            previewUrl = favoriteTrack.previewUrl)
    }
}