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
    private var track : Track? = null
    private val trackSerializer = Creator.provideTrackSerializer()
    private val playerInteractor = Creator.providePlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())
    private val playerRunnable = Runnable { updateTime() }
    private val clickDebouncer = ClickDebouncer(CLICK_DEBOUNCE_DELAY)

    private val playerState = MutableLiveData<PlayerState>(PlayerState.ShowContentNotReady(
        TrackSerializer.EMPTY_TRACK))
    val playerStateLiveData : LiveData<PlayerState> = playerState

    private val startTimeString = application.getString(R.string.default_preview_time)
    private val currentTime = MutableLiveData(startTimeString)
    val currentTimeLiveData : LiveData<String> = currentTime

    fun onCreate(intent: Intent) {
        track = trackSerializer.fromString(intent.getStringExtra("TRACK_JSON") ?: "")
        track?.let {track ->
            playerState.postValue(PlayerState.ShowContentNotReady(track))
            playerInteractor.setOnCompletionListener {
                currentTime.postValue(startTimeString)
                playerState.postValue(PlayerState.ShowContentPause)
            }
            playerInteractor.setOnPreparedListener {
                playerState.postValue(PlayerState.ShowContentPause)
            }
            playerInteractor.init(track.previewUrl)
        } ?: {
            playerState.postValue(PlayerState.ShowContentNotReady(TrackSerializer.EMPTY_TRACK))
        }
    }

    override fun onCleared() {
        clickDebouncer.clearCalls()
        handler.removeCallbacks(playerRunnable)
        playerInteractor.release()
    }

    fun changeState() {
        if (clickDebouncer.can()) {
            when (playerInteractor.isPlaying()) {
                true -> {
                    playerInteractor.pause()
                    playerState.postValue(PlayerState.ShowContentPause)
                }

                else -> {
                    playerInteractor.play()
                    playerState.postValue(PlayerState.ShowContentPlaying)
                }
            }
            updateTime()
        }
    }

    fun pause() {
        if (playerInteractor.pause()) {
            playerState.postValue(PlayerState.ShowContentPause)
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