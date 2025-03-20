package com.ejilonok.playlistmaker.search.domain.api.repository

import com.ejilonok.playlistmaker.search.domain.models.Track

interface SearchHistoryRepository {
    fun load()  : List<Track>
    fun save(tracks : List<Track>)
}