package com.ejilonok.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.library.domain.api.interactor.FavoriteTrackInteractor
import com.ejilonok.playlistmaker.main.domain.ResourceProvider
import com.ejilonok.playlistmaker.main.presentation.common.debounce
import com.ejilonok.playlistmaker.player.domain.api.interactor.PlayerInteractor
import com.ejilonok.playlistmaker.player.domain.api.mapper.TrackSerializer
import com.ejilonok.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerViewModel(
    resourceProvider: ResourceProvider,
    private val trackSerializer : TrackSerializer,
    private val playerInteractor : PlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor
) : ViewModel() {
    lateinit var changeStateDebounce: () -> Unit

    private var updateStateJob : Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.NoTrack)
    val playerStateLiveData : LiveData<PlayerState> = playerState

    private fun getActualTrack() : Track {
        return if (playerState.value is PlayerState.Content)
            (playerState.value as PlayerState.Content).track
        else TrackSerializer.EMPTY_TRACK
    }

    private val startTimeString = resourceProvider.getString(R.string.default_preview_time)
    private val currentTime = MutableLiveData(startTimeString)

    private val isFavorite = MutableLiveData(false)
    val isFavoriteLiveData : LiveData<Boolean> = isFavorite

    val currentTimeLiveData : LiveData<String> = currentTime

    fun onCreate(params : String) {
        setTrack(params)

        changeStateDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewModelScope, false) {
            changeState()
        }
    }

    private fun setTrack(params : String) {
        val trackFromIntent = trackSerializer.fromString(params)
        when (playerState.value) {
            is PlayerState.Content -> {
                val actualTrack = getActualTrack()
                if (trackFromIntent != actualTrack) {
                    stopASync()

                    bindTrack(trackFromIntent)
                }

            }
            is PlayerState.NoTrack -> bindTrack(trackFromIntent)
            else -> {}
        }
    }

    private fun bindTrack(track : Track) {
        playerState.postValue(PlayerState.Content.ShowContentNotReady(track))
        playerInteractor.setOnCompletionListener {
            currentTime.postValue(startTimeString)
            playerState.postValue(PlayerState.Content.ShowContentPause(track))
        }
        playerInteractor.setOnPreparedListener {
            playerState.postValue(PlayerState.Content.ShowContentPause(track))
        }
        playerInteractor.init(track.previewUrl)
        viewModelScope.launch {
            favoriteTrackInteractor.isTrackFavorite(track).collect {
                isFavorite.postValue(it)
            }
        }
    }

    override fun onCleared() {
        stopASync()
    }

    private fun stopASync() {
        playerInteractor.release()
    }

    private fun changeState() {
        updateStateJob?.cancel()
        when (playerInteractor.isPlaying()) {
            true -> pause()
            else -> {
                play()
                updateStateJob = viewModelScope.launch {
                    updateTime()
                }
            }
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

    private suspend fun updateTime() {
        while (playerInteractor.isPlaying()) {
            delay(DELAY_UPDATE)
            currentTime.postValue(playerInteractor.getCurrentTimeString())
        }
    }

    fun changeFavorite() {
        viewModelScope.launch {
            val actual_track = getActualTrack()
            if ((isFavorite.value == null) || !(isFavorite.value!!)) {
                favoriteTrackInteractor.addTrackToFavorite(actual_track)
                isFavorite.postValue(true)
            } else {
                favoriteTrackInteractor.removeTrackFromFavorite(actual_track)
                isFavorite.postValue(false)
            }
        }
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 150L
        const val DELAY_UPDATE = 300L
    }
}
