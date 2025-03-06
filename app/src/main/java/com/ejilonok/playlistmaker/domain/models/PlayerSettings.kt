package com.ejilonok.playlistmaker.domain.models

data class PlayerSettings(
    var state: STATES = DEFAULT_STATE,
    var position: Int = DEFAULT_POSITION ) {

    companion object {
        enum class STATES {
            STATE_PAUSED,
            STATE_PLAY
        }
        const val DEFAULT_POSITION = 0
        val DEFAULT_STATE = STATES.STATE_PAUSED
    }
}