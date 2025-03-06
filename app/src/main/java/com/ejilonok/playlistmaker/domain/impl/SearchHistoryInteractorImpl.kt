package com.ejilonok.playlistmaker.domain.impl

import com.ejilonok.playlistmaker.domain.api.interactors.SearchHistoryInteractor
import com.ejilonok.playlistmaker.domain.api.repository.SearchHistoryRepository
import com.ejilonok.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(private val searchHistoryRepository : SearchHistoryRepository) : SearchHistoryInteractor {
    private val tracks : ArrayList<Track> = ArrayList()
    override fun clear() {
        tracks.clear()
        searchHistoryRepository.save( tracks )
    }

    override fun addTrack(track: Track, consumer: SearchHistoryInteractor.SearchHistoryConsumer) {
        if (tracks.contains(track)) {
            tracks.remove(track)
        }
        else if (!canMoreAdd()) {
            tracks.removeAt(HISTORY_MAX_SIZE - 1)
        }

        tracks.add(0, track)
        searchHistoryRepository.save( tracks )

        consumer.consume(tracks)
    }

    override fun load() : List<Track> {
        tracks.clear()
        tracks.addAll( searchHistoryRepository.load() )

        return tracks
    }

    override fun isHistoryEmpty(): Boolean {
        return tracks.isEmpty()
    }

    private fun canMoreAdd() : Boolean {
        return tracks.size < HISTORY_MAX_SIZE
    }

    companion object {
        const val HISTORY_MAX_SIZE = 10
    }
}