package com.ejilonok.playlistmaker.search.domain.api.interactor

import com.ejilonok.playlistmaker.main.domain.consumer.ConsumerData
import com.ejilonok.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTracks(expression: String) : Flow<ConsumerData<List<Track>>>
    fun isNetworkConnected() : Boolean
}