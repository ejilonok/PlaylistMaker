package com.ejilonok.playlistmaker.player.domain.impl

import android.media.MediaPlayer
import com.ejilonok.playlistmaker.main.domain.consumer.SimpleConsumer
import com.ejilonok.playlistmaker.player.domain.api.interactor.PlayerInteractor
import com.ejilonok.playlistmaker.player.domain.api.repository.PlayerSettingsRepository
import com.ejilonok.playlistmaker.player.domain.models.PlayerSettings
import com.ejilonok.playlistmaker.player.domain.models.PlayerSettings.Companion.STATES.*
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerInteractorImpl(
    private val playerSettingsRepository: PlayerSettingsRepository
) : PlayerInteractor {
    // TODO: нужно перенести mediaplayer или в data, или в ui
    private val mediaPlayer = MediaPlayer()
    private var playerSettings = PlayerSettings()
    private val formater = SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun loadState(state : String?) {
        playerSettings = playerSettingsRepository.load(state)
    }

    override fun save() : String {
        pause()
        return playerSettingsRepository.save(playerSettings)
    }

    override fun init(source : String) {
        mediaPlayer.setDataSource(source)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnCompletionListener { reset() }
    }


    override fun setOnPreparedListener(onPreparedListener: SimpleConsumer) {
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.seekTo(playerSettings.position)
            onPreparedListener.consume()
        }
    }

    override fun setOnCompleteListener(onCompletionListener: SimpleConsumer) {
        mediaPlayer.setOnCompletionListener {
            reset()
            onCompletionListener.consume()
        }
    }

    override fun isPlaying() : Boolean {
        return playerSettings.state == STATE_PLAY
    }

    override fun play() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            playerSettings.state = STATE_PLAY
        }
    }
    override fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            playerSettings.state = STATE_PAUSED
        }
    }

    private fun reset() {
        playerSettings.state = STATE_PAUSED
        mediaPlayer.seekTo(0)
    }

    override fun getCurrentTimeString() : String {
        return formater.format( mediaPlayer.currentPosition )
    }
}