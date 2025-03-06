package com.ejilonok.playlistmaker.domain.api.interactors

import com.ejilonok.playlistmaker.domain.consumer.SimpleConsumer

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