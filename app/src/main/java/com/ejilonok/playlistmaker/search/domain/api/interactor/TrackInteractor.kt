package com.ejilonok.playlistmaker.search.domain.api.interactor

import com.ejilonok.playlistmaker.main.domain.consumer.Consumer
import com.ejilonok.playlistmaker.main.domain.consumer.ConsumerData
import com.ejilonok.playlistmaker.search.domain.models.Track

interface TrackInteractor {
    fun searchTracks(expression: String, consumer : TracksConsumer)
    fun isNetworkConnected() : Boolean
    fun interface TracksConsumer : Consumer<List<Track>> {
        override fun consume(data: ConsumerData<List<Track>>)
    }
}