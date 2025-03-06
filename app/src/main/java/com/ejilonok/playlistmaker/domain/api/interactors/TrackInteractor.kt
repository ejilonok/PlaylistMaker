package com.ejilonok.playlistmaker.domain.api.interactors

import com.ejilonok.playlistmaker.domain.consumer.Consumer
import com.ejilonok.playlistmaker.domain.consumer.ConsumerData
import com.ejilonok.playlistmaker.domain.models.Track

interface TrackInteractor {
    fun searchTracks(expression: String, consumer : TracksConsumer)

    fun interface TracksConsumer : Consumer<List<Track>> {
        override fun consume(data: ConsumerData<List<Track>>)
    }
}