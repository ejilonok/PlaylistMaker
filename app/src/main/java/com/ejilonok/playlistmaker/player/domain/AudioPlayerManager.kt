package com.ejilonok.playlistmaker.player.domain

import com.ejilonok.playlistmaker.main.domain.consumer.SimpleConsumer

interface AudioPlayerManager {
    fun load(source: String)
    fun release()

    fun setOnPreparedListener(onPreparedListener: SimpleConsumer)
    fun setOnCompletionListener(onCompletionListener: SimpleConsumer)
    fun isPlaying(): Boolean
    fun play(): Boolean
    fun pause(): Boolean
    fun reset()
    fun getCurrentPosition(): Int
    fun setPosition(position: Int)
}