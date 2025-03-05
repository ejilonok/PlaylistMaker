package com.ejilonok.playlistmaker.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ejilonok.playlistmaker.PlaylistMakerApplication
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.domain.models.Track
import com.ejilonok.playlistmaker.databinding.ActivityPlayerBinding
import com.ejilonok.playlistmaker.presentation.GraphicUtils
import com.ejilonok.playlistmaker.presentation.tracks.TrackExtensions.getTrackArtworkUrl512

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPlayerBinding
    private val playerInteractor = Creator.providePlayerInteractor()
    private var track : Track? = null
    private val handler = Handler(Looper.getMainLooper())
    private val playerRunnable = Runnable { updatePlayerInfo() }

    companion object {
        private const val DELAY_UPDATE = 250L
        private const val SETTINGS_TAG = "SETTINGS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadPlayerData(savedInstanceState)

        track = (application as PlaylistMakerApplication).actualTrack

        enablePlayButton(false)

        loadPlayerData(savedInstanceState)

        binding.playButton.setOnClickListener { changeState() }
        playerInteractor.setOnCompleteListener { setPlayButton() }
        binding.playerBackButton.setOnClickListener { finish() }
        playerInteractor.setOnPreparedListener { enablePlayButton() }

        bindTrack()
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

    override fun onPause() {
        super.onPause()
        playerInteractor.pause()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        playerInteractor.pause()
        outState.putString(SETTINGS_TAG, playerInteractor.save())
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        loadPlayerData(savedInstanceState)
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
            binding.collectionGroup.visibility = View.GONE
        } else {
            binding.playerCollectionNameTv.text = track!!.collectionName
            binding.playerCollectionNameTv.isSelected = true
            binding.collectionGroup.visibility = View.VISIBLE
        }
    }

    private fun bindReleaseDate() {
        if (track == null) {
            return
        }

        if (track!!.releaseDate.isEmpty()) {
            binding.yearGroup.visibility = View.GONE
        } else {
            binding.yearGroup.visibility = View.VISIBLE
            binding.playerYearTv.text = track!!.releaseDate.take(4)
        }
    }

    private fun bindGenre() {
        if (track == null)
            return

        if (track!!.primaryGenreName.isEmpty()) {
            binding.genreGroup.visibility = View.GONE
        } else {
            binding.genreGroup.visibility = View.VISIBLE
            binding.playerGenreTv.text = track!!.primaryGenreName
        }
    }

    private fun bindCountry() {
        if (track == null) {
            return
        }

        if (track!!.country.isEmpty()) {
            binding.countryGroup.visibility = View.GONE
        } else {
            binding.countryGroup.visibility = View.VISIBLE
            binding.playerCountryTv.text = track!!.country
        }
    }

    private fun updatePlayerInfo() {
        if (playerInteractor.isPlaying()) {
            handler.postDelayed(playerRunnable, DELAY_UPDATE)
        }

        binding.currentTimeTv.text = playerInteractor.getCurrentTimeString()
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
}