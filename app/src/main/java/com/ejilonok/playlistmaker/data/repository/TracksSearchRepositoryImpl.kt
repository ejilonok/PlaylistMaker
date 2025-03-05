package com.ejilonok.playlistmaker.data.repository

import com.ejilonok.playlistmaker.data.NetworkClient
import com.ejilonok.playlistmaker.data.dto.TrackMapper
import com.ejilonok.playlistmaker.data.dto.TracksSearchRequest
import com.ejilonok.playlistmaker.data.dto.TracksSearchResponse
import com.ejilonok.playlistmaker.domain.api.repository.TracksSearchRepository
import com.ejilonok.playlistmaker.domain.models.Resource
import com.ejilonok.playlistmaker.domain.models.Track

class TracksSearchRepositoryImpl(private val networkClient: NetworkClient) : TracksSearchRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return if (response.resultCode == 200) {
            Resource.Success (( response as TracksSearchResponse).results.map { TrackMapper.map(it) } )
        } else {
            Resource.Error ( Resource.ResourceErrorCodes.NETWORK_ERROR.code )
        }
    }
}
