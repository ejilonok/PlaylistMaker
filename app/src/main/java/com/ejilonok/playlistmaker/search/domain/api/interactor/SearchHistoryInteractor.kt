package com.ejilonok.playlistmaker.search.domain.api.interactor

import com.ejilonok.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun clear()
    fun addTrack(track : Track, consumer : SearchHistoryConsumer)
    fun load() : List<Track>

    fun isHistoryEmpty() : Boolean

    interface SearchHistoryConsumer {
        fun consume(actualHistory: List<Track>)
    }
}