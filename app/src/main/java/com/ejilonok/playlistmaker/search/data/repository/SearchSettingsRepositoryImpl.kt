package com.ejilonok.playlistmaker.search.data.repository

import com.ejilonok.playlistmaker.search.data.dto.SearchSettingsDto
import com.ejilonok.playlistmaker.search.data.dto.SearchSettingsMapper
import com.ejilonok.playlistmaker.search.domain.api.repository.SearchSettingsRepository
import com.ejilonok.playlistmaker.search.domain.models.SearchSettings

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