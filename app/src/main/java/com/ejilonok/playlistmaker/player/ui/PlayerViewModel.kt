package com.ejilonok.playlistmaker.player.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.databinding.ActivityPlayerBinding
import com.ejilonok.playlistmaker.main.ui.common.GraphicUtils
import com.ejilonok.playlistmaker.main.ui.common.gone
import com.ejilonok.playlistmaker.main.ui.common.show
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.google.gson.Gson


class PlayerViewModel(
    private val activity: Activity,
    private val binding: ActivityPlayerBinding
) {
    private var track : Track? = null
    private val playerInteractor = Creator.providePlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())
    private val playerRunnable = Runnable { updatePlayerInfo() }
    fun onCreate(intent: Intent) {
        try {
            val trackJson = intent.getStringExtra("TRACK_JSON")
            track = Gson().fromJson( trackJson, Track::class.java )
        } catch (e : Exception) {
            track = Track(
                0, // Уникальный идентификатор композиции, primary key.
            "-----", // Название композиции
                "-----", // Имя исполнителя
            "0:00", // Продолжительность трека
            "", // Ссылка на изображение обложки
            "-----", // Название альбома
            "0000", // Год релиза трека
            "------", // Жанр трека
            "------", // Страна исполнителя
            "")
        }

        enablePlayButton(false)

        binding.playButton.setOnClickListener { changeState() }
        playerInteractor.setOnCompleteListener { setPlayButton() }
        binding.playerBackButton.setOnClickListener { activity.finish() }
        playerInteractor.setOnPreparedListener { enablePlayButton() }

        bindTrack()
    }

    fun onDestroy() {

    }

    fun load(savedInstanceState: Bundle?) {
        loadPlayerData(savedInstanceState)
    }
    fun save(outState: Bundle) {
        pause()
        outState.putString(SETTINGS_TAG, playerInteractor.save())
    }

    private fun enablePlayButton(enable : Boolean = true) {
        binding.playButton.isEnabled = enable
    }

    private fun setPlayButton() {
        binding.playButton.setImageResource(R.drawable.play_button)
    }

    private fun setPauseButton() {
        binding.playButton.setImageResource(R.drawable.pause_button)
    }

    private fun loadPlayerData(savedInstanceState: Bundle?) {
        val settings = savedInstanceState?.getString(SETTINGS_TAG)
        playerInteractor.loadState(settings)
    }

    fun pause() {
        playerInteractor.pause()
    }

    private fun changeState() {
        when (playerInteractor.isPlaying()) {
            true -> {
                playerInteractor.pause()
                setPlayButton()
            }
            else -> {
                playerInteractor.play()
                setPauseButton()
            }
        }
        updatePlayerInfo()
    }

    private fun updatePlayerInfo() {
        if (playerInteractor.isPlaying()) {
            handler.postDelayed(playerRunnable, DELAY_UPDATE)
        }

        binding.currentTimeTv.text = playerInteractor.getCurrentTimeString()
    }

    private fun bindTrack() {
        if (track == null) return
        val track = track as Track

        binding.playerTrackName.text = track.trackName
        binding.playerTrackName.isSelected = true            // to start animation
        binding.playerArtistName.text = track.artistName
        binding.playerArtistName.isSelected = true           // to start animation
        binding.playerTrackTimeTv.text = track.trackTime

        bindCover()
        bindCollection()
        bindReleaseDate()
        bindGenre()
        bindCountry()

        playerInteractor.init(track.previewUrl)
    }

    //TODO перенести запрос изображения в интерактор.
    private fun bindCover() {
        Glide.with(binding.root)
            .load(track?.getTrackArtworkUrl512() ?: "")
            .placeholder(R.drawable.cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(GraphicUtils.dpToPx(8.0f, binding.root)))
            .into(binding.playerTrackCover)
    }

    private fun bindCollection() {
        if (track?.collectionName.isNullOrEmpty()) {
            binding.collectionGroup.gone()
        } else {
            binding.playerCollectionNameTv.text = track!!.collectionName
            binding.playerCollectionNameTv.isSelected = true
            binding.collectionGroup.show()
        }
    }

    private fun bindReleaseDate() {
        if (track == null) {
            return
        }

        if (track!!.releaseDate.isEmpty()) {
            binding.yearGroup.gone()
        } else {
            binding.yearGroup.show()
            binding.playerYearTv.text = track!!.releaseDate.take(4)
        }
    }

    private fun bindGenre() {
        if (track == null)
            return

        if (track!!.primaryGenreName.isEmpty()) {
            binding.genreGroup.gone()
        } else {
            binding.genreGroup.show()
            binding.playerGenreTv.text = track!!.primaryGenreName
        }
    }

    private fun bindCountry() {
        if (track == null) {
            return
        }

        if (track!!.country.isEmpty()) {
            binding.countryGroup.gone()
        } else {
            binding.countryGroup.show()
            binding.playerCountryTv.text = track!!.country
        }
    }
    companion object {
        private const val DELAY_UPDATE = 250L
        private const val SETTINGS_TAG = "SETTINGS"
    }
}