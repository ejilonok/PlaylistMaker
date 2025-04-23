package com.ejilonok.playlistmaker.player.domain.api.mapper

import com.ejilonok.playlistmaker.search.domain.models.Track

interface TrackSerializer {
    fun toString(track: Track) : String
    fun fromString(string: String) : Track
}