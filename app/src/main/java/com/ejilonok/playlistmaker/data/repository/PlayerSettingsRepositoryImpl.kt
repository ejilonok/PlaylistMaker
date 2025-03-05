package com.ejilonok.playlistmaker.data.repository

import com.ejilonok.playlistmaker.domain.api.repository.PlayerSettingsRepository
import com.ejilonok.playlistmaker.domain.models.PlayerSettings
import com.ejilonok.playlistmaker.domain.models.PlayerSettings.Companion.DEFAULT_POSITION
import com.ejilonok.playlistmaker.domain.models.PlayerSettings.Companion.DEFAULT_STATE
import com.ejilonok.playlistmaker.domain.models.PlayerSettings.Companion.STATES

class PlayerSettingsRepositoryImpl : PlayerSettingsRepository {
    override fun load(text : String?) : PlayerSettings {
        if (text.isNullOrEmpty())
            return PlayerSettings()

        val values = text.split(",")
        if (values.count() != 2)
            return PlayerSettings()

        var state = values[0].toIntOrNull()
        var position = values[1].toIntOrNull()
        if (state == null) {
            state = DEFAULT_STATE.ordinal
        }
        if (position == null) {
            position = DEFAULT_POSITION
        }
        return try {
            PlayerSettings(STATES.entries[state], position)
        } catch (e : Exception) {
            PlayerSettings(DEFAULT_STATE, position)
        }
    }

    override fun save(settings : PlayerSettings) : String {
        return "${settings.state.ordinal},${settings.position}"
    }
}