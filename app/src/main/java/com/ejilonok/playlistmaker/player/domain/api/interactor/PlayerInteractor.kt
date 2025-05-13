package com.ejilonok.playlistmaker.player.domain.api.interactor

import com.ejilonok.playlistmaker.main.domain.consumer.SimpleConsumer

interface PlayerInteractor {
    fun init(source : String)
    fun release()
    fun setOnPreparedListener(onPreparedListener: SimpleConsumer)
    fun setOnCompletionListener(onCompletionListener: SimpleConsumer)
    fun isPlaying() : Boolean
    fun play() : Boolean
    fun pause() : Boolean
    fun getCurrentTimeString() : String
}