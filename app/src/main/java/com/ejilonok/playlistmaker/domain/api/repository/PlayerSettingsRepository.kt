package com.ejilonok.playlistmaker.domain.api.repository

import com.ejilonok.playlistmaker.domain.models.PlayerSettings

interface PlayerSettingsRepository {
    fun load(text: String?): PlayerSettings
    fun save(settings: PlayerSettings): String
}
