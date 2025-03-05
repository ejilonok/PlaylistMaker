package com.ejilonok.playlistmaker.domain.impl

import com.ejilonok.playlistmaker.domain.api.interactors.TrackInteractor
import com.ejilonok.playlistmaker.domain.api.repository.TracksSearchRepository
import com.ejilonok.playlistmaker.domain.consumer.ConsumerData
import com.ejilonok.playlistmaker.domain.models.Resource
import com.ejilonok.playlistmaker.domain.models.ResponseCode
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository : TracksSearchRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            when (val result = repository.searchTracks(expression)) {
                is Resource.Success -> consumer.consume( ConsumerData.Data(result.data) )
                is Resource.Error -> consumer.consume( ConsumerData.Error(ResponseCode.NO_ANSWER.code) )
            }
        }
    }
}