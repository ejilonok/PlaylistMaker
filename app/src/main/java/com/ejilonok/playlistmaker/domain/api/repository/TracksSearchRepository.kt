package com.ejilonok.playlistmaker.domain.api.repository

import com.ejilonok.playlistmaker.domain.models.Resource
import com.ejilonok.playlistmaker.domain.models.Track

interface TracksSearchRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}