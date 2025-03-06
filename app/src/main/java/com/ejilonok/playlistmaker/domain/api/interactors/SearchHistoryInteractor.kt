package com.ejilonok.playlistmaker.domain.api.interactors

import com.ejilonok.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun clear()
    fun addTrack(track : Track, consumer : SearchHistoryConsumer)
    fun load() : List<Track>

    fun isHistoryEmpty() : Boolean

    interface SearchHistoryConsumer {
        fun consume(actualHistory: List<Track>)
    }
}