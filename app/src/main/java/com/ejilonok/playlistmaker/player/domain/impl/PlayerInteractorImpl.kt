package com.ejilonok.playlistmaker.player.domain.impl

import com.ejilonok.playlistmaker.main.domain.consumer.SimpleConsumer
import com.ejilonok.playlistmaker.player.domain.AudioPlayerManager
import com.ejilonok.playlistmaker.player.domain.api.interactor.PlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerInteractorImpl(
    private val audioManager: AudioPlayerManager
) : PlayerInteractor {
    private val formater = SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun init(source : String) = audioManager.load(source)

    override fun release() = audioManager.release()

    override fun setOnPreparedListener(onPreparedListener: SimpleConsumer) {
        audioManager.setOnPreparedListener {
            onPreparedListener.consume()
        }
    }

    override fun setOnCompletionListener(onCompletionListener: SimpleConsumer) {
        audioManager.setOnCompletionListener {
            onCompletionListener.consume()
        }
    }

    override fun isPlaying() : Boolean = audioManager.isPlaying()
    override fun play() = audioManager.play()
    override fun pause() = audioManager.pause()
    override fun getCurrentTimeString() : String = formater.format( audioManager.getCurrentPosition() )
}