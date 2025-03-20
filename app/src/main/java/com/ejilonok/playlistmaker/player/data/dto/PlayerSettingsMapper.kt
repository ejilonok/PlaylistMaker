package com.ejilonok.playlistmaker.player.data.dto

import com.ejilonok.playlistmaker.player.domain.models.PlayerSettings

object PlayerSettingsMapper {
    fun map(playerSettingsDto: PlayerSettingsDto) : PlayerSettings {
        return PlayerSettings(playerSettingsDto.state, playerSettingsDto.position)
    }
}