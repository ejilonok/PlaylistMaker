package com.ejilonok.playlistmaker.search.domain.api.repository

import com.ejilonok.playlistmaker.search.domain.models.SearchSettings

interface SearchSettingsRepository {
    fun save(searchSettings: SearchSettings) : String
    fun load(data : String) : SearchSettings
}
