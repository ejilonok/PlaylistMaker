package com.ejilonok.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.ejilonok.playlistmaker.main.domain.consumer.SimpleConsumer
import com.ejilonok.playlistmaker.player.domain.AudioPlayerManager

class AudioPlayerManagerImpl(
    private val mediaPlayer : MediaPlayer
) : AudioPlayerManager {
    private var notPrepared = true

    override fun load(source : String) {
        mediaPlayer.apply {
            setDataSource(source)
            notPrepared = true
            prepareAsync()
        }
    }

    override fun release() {
        mediaPlayer.setOnCompletionListener(null)
        mediaPlayer.setOnPreparedListener(null)

        if (mediaPlayer.isPlaying || notPrepared)
            mediaPlayer.stop()
    }
    override fun setOnPreparedListener(onPreparedListener: SimpleConsumer) {
        mediaPlayer.setOnPreparedListener {
            onPreparedListener.consume()
            notPrepared = true
        }
    }

    override fun setOnCompletionListener(onCompletionListener: SimpleConsumer) {
        mediaPlayer.setOnCompletionListener {
            reset()
            onCompletionListener.consume()
        }
    }

    override fun isPlaying() : Boolean {
        return mediaPlayer.isPlaying
    }

    override fun play() : Boolean {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            return true
        }
        return false
    }
    override fun pause() : Boolean {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            return true
        }
        return false
    }

    override fun reset() {
        mediaPlayer.pause()
        mediaPlayer.seekTo(0)
    }

    override fun getCurrentPosition() : Int {
        return mediaPlayer.currentPosition
    }

    override fun setPosition(position : Int) {
        mediaPlayer.seekTo(position)
    }
}