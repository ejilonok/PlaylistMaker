package com.ejilonok.playlistmaker.data.dto

import com.ejilonok.playlistmaker.domain.models.SearchSettings

object SearchSettingsMapper {
    fun map(settings : SearchSettingsDto) : SearchSettings {
        return SearchSettings(settings.searchString)
    }

    fun map(settings : SearchSettings) : SearchSettingsDto {
        return SearchSettingsDto(settings.searchString)
    }
}