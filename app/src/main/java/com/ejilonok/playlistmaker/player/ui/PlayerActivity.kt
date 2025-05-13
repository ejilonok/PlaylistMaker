package com.ejilonok.playlistmaker.player.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.databinding.ActivityPlayerBinding
import com.ejilonok.playlistmaker.main.ui.common.GraphicUtils
import com.ejilonok.playlistmaker.main.ui.common.gone
import com.ejilonok.playlistmaker.main.ui.common.show
import com.ejilonok.playlistmaker.player.presentation.PlayerState
import com.ejilonok.playlistmaker.player.presentation.PlayerViewModel
import com.ejilonok.playlistmaker.search.domain.models.Track

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPlayerBinding
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerViewModel = ViewModelProvider(this,
            PlayerViewModel.getViewModelFactory())[PlayerViewModel::class.java]
        playerViewModel.onCreate(intent)
        playerViewModel.playerStateLiveData.observe(this) {state ->
            renderState(state)
        }
        playerViewModel.currentTimeLiveData.observe(this) {
            binding.currentTimeTv.text = it
        }

        binding.playButton.setOnClickListener { playerViewModel.changeState() }
        binding.playerBackButton.setOnClickListener { playerViewModel.finish() }
    }

    private fun renderState(state : PlayerState) {
        when (state) {
            is PlayerState.Content.ShowContentNotReady -> {
                bindTrack(state.track)
                setPlayButtonEnabled(false)
            }
            is PlayerState.Content.ShowContentPause -> {
                bindTrack(state.track)
                setPlayButton()
                setPlayButtonEnabled(true)
            }
            is PlayerState.Content.ShowContentPlaying -> {
                bindTrack(state.track)
                setPauseButton()
                setPlayButtonEnabled(true)
            }
            is PlayerState.Finish -> finish()
            is PlayerState.NoTrack -> {}
        }
    }

    private fun bindTrack(track : Track) {
        binding.playerTrackName.text = track.trackName
        binding.playerTrackName.isSelected = true            // to start animation
        binding.playerArtistName.text = track.artistName
        binding.playerArtistName.isSelected = true           // to start animation
        binding.playerTrackTimeTv.text = track.trackTime

        bindCover(track)
        bindCollection(track)
        bindReleaseDate(track)
        bindGenre(track)
        bindCountry(track)
    }

    private fun bindCover(track: Track) {
        Glide.with(binding.root)
            .load(track.getTrackArtworkUrl512())
            .placeholder(R.drawable.cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(GraphicUtils.dpToPx(8.0f, binding.root)))
            .into(binding.playerTrackCover)
    }

    private fun bindCollection(track: Track) {
        if (track.collectionName.isEmpty()) {
            binding.collectionGroup.gone()
        } else {
            binding.playerCollectionNameTv.text = track.collectionName
            binding.playerCollectionNameTv.isSelected = true
            binding.collectionGroup.show()
        }
    }

    private fun bindReleaseDate(track: Track) {
        if (track.releaseDate.isEmpty()) {
            binding.yearGroup.gone()
        } else {
            binding.yearGroup.show()
            binding.playerYearTv.text = track.releaseDate.take(4)
        }
    }

    private fun bindGenre(track: Track) {
        if (track.primaryGenreName.isEmpty()) {
            binding.genreGroup.gone()
        } else {
            binding.genreGroup.show()
            binding.playerGenreTv.text = track.primaryGenreName
        }
    }

    private fun bindCountry(track: Track) {
        if (track.country.isEmpty()) {
            binding.countryGroup.gone()
        } else {
            binding.countryGroup.show()
            binding.playerCountryTv.text = track.country
        }
    }

    private fun setPlayButton() {
        binding.playButton.setImageResource(R.drawable.play_button)
    }

    private fun setPauseButton() {
        binding.playButton.setImageResource(R.drawable.pause_button)
    }

    private fun setPlayButtonEnabled(enabled : Boolean) {
        binding.playButton.isEnabled = enabled
    }
}