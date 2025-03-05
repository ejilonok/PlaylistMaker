package com.ejilonok.playlistmaker.data.repository

import com.ejilonok.playlistmaker.data.dto.SearchSettingsDto
import com.ejilonok.playlistmaker.data.dto.SearchSettingsMapper
import com.ejilonok.playlistmaker.domain.api.repository.SearchSettingsRepository
import com.ejilonok.playlistmaker.domain.models.SearchSettings

class SearchSettingsRepositoryImpl : SearchSettingsRepository {
    override fun save(searchSettings: SearchSettings) : String {
        val searchSettingsDto = SearchSettingsMapper.map(searchSettings)
        return searchSettingsDto.searchString
        //state.putString(SEARCH_STRING, settings.searchString)
    }

    override fun load(data : String) : SearchSettings {
        val searchSettingsDto = SearchSettingsDto(data)
        return SearchSettingsMapper.map(searchSettingsDto)
            //state.getString(SEARCH_STRING) ?: SEARCH_STRING_DEF
    }
}