package com.ejilonok.playlistmaker.player.presentation

import com.ejilonok.playlistmaker.search.domain.models.Track

sealed interface PlayerState {
    data class ShowContentNotReady(val track: Track) : PlayerState
    object ShowContentPlaying : PlayerState
    object ShowContentPause : PlayerState
    object Finish : PlayerState
}