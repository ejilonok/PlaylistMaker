package com.ejilonok.playlistmaker.player.domain.api.interactor

import com.ejilonok.playlistmaker.main.domain.consumer.SimpleConsumer

interface PlayerInteractor {
    fun loadState(state : String?)
    fun save() : String
    fun init(source : String)
    fun setOnPreparedListener(onPreparedListener: SimpleConsumer)
    fun setOnCompleteListener(onCompletionListener: SimpleConsumer)
    fun isPlaying() : Boolean
    fun play()
    fun pause()
    fun getCurrentTimeString() : String
}