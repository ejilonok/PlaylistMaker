package com.ejilonok.playlistmaker.search.domain.api.repository

import com.ejilonok.playlistmaker.search.domain.models.Resource
import com.ejilonok.playlistmaker.search.domain.models.Track

interface TracksSearchRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
    fun isNetworkConnected() : Boolean
}