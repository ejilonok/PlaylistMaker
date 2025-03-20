package com.ejilonok.playlistmaker.player.data.dto

import com.ejilonok.playlistmaker.player.domain.models.PlayerSettings

data class PlayerSettingsDto(
    var state: PlayerSettings.Companion.STATES = PlayerSettings.DEFAULT_STATE,
    var position: Int = PlayerSettings.DEFAULT_POSITION ) {
}