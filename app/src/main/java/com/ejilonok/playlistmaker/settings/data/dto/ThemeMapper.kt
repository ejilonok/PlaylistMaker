package com.ejilonok.playlistmaker.settings.data.dto

import com.ejilonok.playlistmaker.settings.domain.models.Theme

object ThemeMapper {
    fun map(theme : ThemeDto) : Theme {
        return Theme(theme.currentTheme)
    }

    fun map(theme : Theme) : ThemeDto {
        return ThemeDto(theme.currentTheme)
    }
}