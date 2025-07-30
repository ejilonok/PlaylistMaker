package com.ejilonok.playlistmaker.search.domain.api.repository

import com.ejilonok.playlistmaker.search.domain.models.Resource
import com.ejilonok.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksSearchRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
    fun isNetworkConnected() : Boolean
}