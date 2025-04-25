package com.ejilonok.playlistmaker.player.presentation

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.main.presentation.common.ClickDebouncer
import com.ejilonok.playlistmaker.player.domain.api.mapper.TrackSerializer
import com.ejilonok.playlistmaker.search.domain.models.Track


class PlayerViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val trackSerializer = Creator.provideTrackSerializer()
    private val playerInteractor = Creator.providePlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())
    private val playerRunnable = Runnable { updateTime() }
    private val clickDebouncer = ClickDebouncer(CLICK_DEBOUNCE_DELAY)

    private val playerState = MutableLiveData<PlayerState>(PlayerState.NoTrack)
    val playerStateLiveData : LiveData<PlayerState> = playerState

    private fun getActualTrack() : Track {
        return if (playerState.value is PlayerState.Content)
            (playerState.value as PlayerState.Content).track
        else TrackSerializer.EMPTY_TRACK
    }

    private val startTimeString = application.getString(R.string.default_preview_time)
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
        handler.removeCallbacks(playerRunnable)
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
            handler.postDelayed(playerRunnable, DELAY_UPDATE)
        }

        currentTime.postValue(playerInteractor.getCurrentTimeString())
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 600L
        private const val DELAY_UPDATE = 350L
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    PlayerViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
                }
            }
    }
}