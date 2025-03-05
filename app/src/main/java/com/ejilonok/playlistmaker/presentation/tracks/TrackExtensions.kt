package com.ejilonok.playlistmaker.presentation.tracks

import com.ejilonok.playlistmaker.domain.models.Track

object TrackExtensions {
    fun Track.getTrackArtworkUrl512() : String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }
}