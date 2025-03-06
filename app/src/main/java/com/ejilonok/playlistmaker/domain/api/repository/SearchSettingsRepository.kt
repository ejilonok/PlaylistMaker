package com.ejilonok.playlistmaker.domain.api.repository

import com.ejilonok.playlistmaker.domain.models.SearchSettings

interface SearchSettingsRepository {
    fun save(searchSettings: SearchSettings) : String
    fun load(data : String) : SearchSettings
}
