package com.ejilonok.playlistmaker.search.data.repository

import com.ejilonok.playlistmaker.search.data.network.NetworkClient
import com.ejilonok.playlistmaker.search.data.dto.TrackMapper
import com.ejilonok.playlistmaker.search.data.dto.TracksSearchRequest
import com.ejilonok.playlistmaker.search.data.dto.TracksSearchResponse
import com.ejilonok.playlistmaker.search.domain.api.repository.TracksSearchRepository
import com.ejilonok.playlistmaker.search.domain.models.Resource
import com.ejilonok.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksSearchRepositoryImpl(
    private val networkClient: NetworkClient ) : TracksSearchRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> =  flow {
        if (!networkClient.isConnected()) {
            emit(Resource.Error ( Resource.ResourceErrorCodes.NETWORK_DISCONNECTED.code ))
        }
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            val data = ( response as TracksSearchResponse).results.map { TrackMapper.map(it) }
            emit( Resource.Success (data))
        } else {
            emit( Resource.Error ( Resource.ResourceErrorCodes.NETWORK_ERROR.code ))
        }
    }

    override fun isNetworkConnected() : Boolean {
        return networkClient.isConnected()
    }
}
