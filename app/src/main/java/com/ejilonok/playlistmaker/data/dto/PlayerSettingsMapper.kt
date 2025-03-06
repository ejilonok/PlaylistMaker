package com.ejilonok.playlistmaker.data.dto

import com.ejilonok.playlistmaker.domain.models.PlayerSettings

object PlayerSettingsMapper {
    fun map(playerSettingsDto: PlayerSettingsDto) : PlayerSettings {
        return PlayerSettings(playerSettingsDto.state, playerSettingsDto.position)
    }
}