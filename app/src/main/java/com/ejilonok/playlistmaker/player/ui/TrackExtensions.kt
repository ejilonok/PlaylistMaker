package com.ejilonok.playlistmaker.player.ui

import com.ejilonok.playlistmaker.search.domain.models.Track

// TrackExtensions должен быть в domain/data слое?
fun Track.getTrackArtworkUrl512() : String {
    return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}