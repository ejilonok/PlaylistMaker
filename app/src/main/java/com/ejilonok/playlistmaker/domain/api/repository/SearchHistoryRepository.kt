package com.ejilonok.playlistmaker.domain.api.repository

import com.ejilonok.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun load()  : List<Track>
    fun save(tracks : List<Track>)
}