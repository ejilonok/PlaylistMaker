package com.ejilonok.playlistmaker.player.domain.api.repository

import com.ejilonok.playlistmaker.player.domain.models.PlayerSettings

interface PlayerSettingsRepository {
    fun load(text: String?): PlayerSettings
    fun save(settings: PlayerSettings): String
}
