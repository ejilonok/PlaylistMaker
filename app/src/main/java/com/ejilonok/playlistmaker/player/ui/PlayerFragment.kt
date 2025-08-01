package com.ejilonok.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.databinding.FragmentPlayerBinding
import com.ejilonok.playlistmaker.main.ui.common.BindingFragment
import com.ejilonok.playlistmaker.main.ui.common.GraphicUtils
import com.ejilonok.playlistmaker.main.ui.common.gone
import com.ejilonok.playlistmaker.main.ui.common.show
import com.ejilonok.playlistmaker.player.domain.api.mapper.TrackSerializer
import com.ejilonok.playlistmaker.player.presentation.PlayerState
import com.ejilonok.playlistmaker.player.presentation.PlayerViewModel
import com.ejilonok.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent

class PlayerFragment : BindingFragment<FragmentPlayerBinding>() {
    private val playerViewModel: PlayerViewModel by viewModel()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val trackString = arguments?.getString(TRACK_TAG) ?: ""
        playerViewModel.onCreate(trackString)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerViewModel.playerStateLiveData.observe(viewLifecycleOwner) {state ->
            renderState(state)
        }
        playerViewModel.currentTimeLiveData.observe(viewLifecycleOwner) {
            binding.currentTimeTv.text = it
        }
        playerViewModel.isFavoriteLiveData.observe(viewLifecycleOwner) {
            binding.likeButton.setImageResource( if (it) FavoriteIcon.FAVORITE.resId else FavoriteIcon.NOT_FAVORITE.resId)
        }

        binding.playButton.setOnClickListener { playerViewModel.changeStateDebounce() }
        binding.playerBackButton.setOnClickListener { findNavController().navigateUp() }
        binding.likeButton.setOnClickListener { playerViewModel.changeFavorite() }
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

    companion object {
        const val TRACK_TAG = "TRACK_JSON"
        private val trackSerializer : TrackSerializer = KoinJavaComponent.getKoin().get()
        fun createArgs(track: Track) = bundleOf(TRACK_TAG to trackSerializer.toString(track))
        enum class FavoriteIcon(val resId: Int) {
            FAVORITE(R.drawable.like_button_enabled),
            NOT_FAVORITE(R.drawable.like_button_disabled)
        }
    }
}
