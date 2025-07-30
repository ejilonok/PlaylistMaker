package com.ejilonok.playlistmaker.search.domain.impl

import com.ejilonok.playlistmaker.main.domain.consumer.ConsumerData
import com.ejilonok.playlistmaker.search.domain.api.interactor.TrackInteractor
import com.ejilonok.playlistmaker.search.domain.api.repository.TracksSearchRepository
import com.ejilonok.playlistmaker.search.domain.models.Resource
import com.ejilonok.playlistmaker.search.domain.models.ResponseCode
import com.ejilonok.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val repository : TracksSearchRepository) : TrackInteractor {

    override fun searchTracks(expression: String) : Flow<ConsumerData<List<Track>>> {
        return repository.searchTracks(getPreparedSearchString(expression)).map { result ->
            when (result) {
                is Resource.Success -> ConsumerData.Data(result.data)
                is Resource.Error -> ConsumerData.Error(ResponseCode.NO_ANSWER.code)
            }
        }
    }

    override fun isNetworkConnected(): Boolean {
        return repository.isNetworkConnected()
    }

    private fun getPreparedSearchString(string : String): String {
        return string.replace(" ", "+")
    }
}