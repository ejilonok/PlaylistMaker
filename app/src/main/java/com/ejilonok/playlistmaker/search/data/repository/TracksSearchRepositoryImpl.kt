package com.ejilonok.playlistmaker.search.data.repository

import com.ejilonok.playlistmaker.search.data.network.NetworkClient
import com.ejilonok.playlistmaker.search.data.dto.TrackMapper
import com.ejilonok.playlistmaker.search.data.dto.TracksSearchRequest
import com.ejilonok.playlistmaker.search.data.dto.TracksSearchResponse
import com.ejilonok.playlistmaker.search.domain.api.repository.TracksSearchRepository
import com.ejilonok.playlistmaker.search.domain.models.Resource
import com.ejilonok.playlistmaker.search.domain.models.Track

class TracksSearchRepositoryImpl(
    private val networkClient: NetworkClient ) : TracksSearchRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        if (!networkClient.isConnected()) {
            return Resource.Error ( Resource.ResourceErrorCodes.NETWORK_DISCONNECTED.code )
        }
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return if (response.resultCode == 200) {
            Resource.Success (( response as TracksSearchResponse).results.map { TrackMapper.map(it) } )
        } else {
            Resource.Error ( Resource.ResourceErrorCodes.NETWORK_ERROR.code )
        }
    }

    override fun isNetworkConnected() : Boolean {
        return networkClient.isConnected()
    }
}
