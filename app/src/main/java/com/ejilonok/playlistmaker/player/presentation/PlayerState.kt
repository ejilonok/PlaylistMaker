package com.ejilonok.playlistmaker.player.presentation

import com.ejilonok.playlistmaker.search.domain.models.Track

sealed interface PlayerState {
    data object NoTrack : PlayerState
    sealed class Content(open val track: Track) : PlayerState {
        data class ShowContentNotReady(override val track: Track) : Content(track)
        data class ShowContentPlaying(override val track: Track) : Content(track)
        data class ShowContentPause(override val track: Track) : Content(track)
    }
}