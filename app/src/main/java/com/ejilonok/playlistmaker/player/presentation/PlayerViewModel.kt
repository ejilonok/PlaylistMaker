package com.ejilonok.playlistmaker.player.presentation

import android.content.Intent
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.main.domain.ResourceProvider
import com.ejilonok.playlistmaker.main.presentation.common.ClickDebouncer
import com.ejilonok.playlistmaker.player.domain.api.interactor.PlayerInteractor
import com.ejilonok.playlistmaker.player.domain.api.mapper.TrackSerializer
import com.ejilonok.playlistmaker.search.domain.models.Track


class PlayerViewModel(
    resourceProvider: ResourceProvider,
    private val trackSerializer : TrackSerializer,
    private val playerInteractor : PlayerInteractor,
    private val mainHandler : Handler,
    private val clickDebouncer : ClickDebouncer
) : ViewModel() {

    private val playerRunnable = Runnable { updateTime() }

    private val playerState = MutableLiveData<PlayerState>(PlayerState.NoTrack)
    val playerStateLiveData : LiveData<PlayerState> = playerState

    private fun getActualTrack() : Track {
        return if (playerState.value is PlayerState.Content)
            (playerState.value as PlayerState.Content).track
        else TrackSerializer.EMPTY_TRACK
    }

    private val startTimeString = resourceProvider.getString(R.string.default_preview_time)
    private val currentTime = MutableLiveData(startTimeString)
    val currentTimeLiveData : LiveData<String> = currentTime

    fun onCreate(intent: Intent) {
        setTrack(intent)
    }

    private fun setTrack(intent: Intent) {
        val trackFromIntent = trackSerializer.fromString(intent.getStringExtra("TRACK_JSON") ?: "")
        when (playerState.value) {
            is PlayerState.Content -> {
                val actualTrack = getActualTrack()
                if (trackFromIntent != actualTrack) {
                    stopASync()

                    loadTrack(trackFromIntent)
                }

            }
            is PlayerState.NoTrack -> loadTrack(trackFromIntent)
            else -> {}
        }
    }

    private fun loadTrack(track : Track) {
        playerState.postValue(PlayerState.Content.ShowContentNotReady(track))
        playerInteractor.setOnCompletionListener {
            currentTime.postValue(startTimeString)
            playerState.postValue(PlayerState.Content.ShowContentPause(track))
        }
        playerInteractor.setOnPreparedListener {
            playerState.postValue(PlayerState.Content.ShowContentPause(track))
        }
        playerInteractor.init(track.previewUrl)
    }

    fun finish() {
        playerState.postValue(PlayerState.Finish)
    }
    override fun onCleared() {
        stopASync()
    }

    private fun stopASync() {
        clickDebouncer.clearCalls()
        mainHandler.removeCallbacks(playerRunnable)
        playerInteractor.release()
    }

    fun changeState() {
        if (clickDebouncer.can()) {
            when (playerInteractor.isPlaying()) {
                true -> pause()
                else -> play()
            }
            updateTime()
        }
    }

    private fun pause() {
        if (playerInteractor.pause()) {
            playerState.postValue(PlayerState.Content.ShowContentPause(getActualTrack()))
        }
    }

    private fun play() {
        if (playerInteractor.play()) {
            playerState.postValue(PlayerState.Content.ShowContentPlaying(getActualTrack()))
        }
    }

    private fun updateTime() {
        if (playerInteractor.isPlaying()) {
            mainHandler.postDelayed(playerRunnable, DELAY_UPDATE)
        }

        currentTime.postValue(playerInteractor.getCurrentTimeString())
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 600L
        const val DELAY_UPDATE = 350L
    }
}