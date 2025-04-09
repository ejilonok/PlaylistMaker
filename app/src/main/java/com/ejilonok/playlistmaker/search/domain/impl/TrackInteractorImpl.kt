package com.ejilonok.playlistmaker.search.domain.impl

import com.ejilonok.playlistmaker.main.domain.consumer.ConsumerData
import com.ejilonok.playlistmaker.search.domain.api.interactor.TrackInteractor
import com.ejilonok.playlistmaker.search.domain.api.repository.TracksSearchRepository
import com.ejilonok.playlistmaker.search.domain.models.Resource
import com.ejilonok.playlistmaker.search.domain.models.ResponseCode
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository : TracksSearchRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            when (val result = repository.searchTracks(getPreparedSearchString(expression))) {
                is Resource.Success -> consumer.consume( ConsumerData.Data(result.data) )
                is Resource.Error -> consumer.consume( ConsumerData.Error(ResponseCode.NO_ANSWER.code) )
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