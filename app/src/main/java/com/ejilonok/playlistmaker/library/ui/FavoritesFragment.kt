package com.ejilonok.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.databinding.FragmentFavoritesBinding
import com.ejilonok.playlistmaker.library.presentation.FavoritesAction
import com.ejilonok.playlistmaker.library.presentation.FavoritesState
import com.ejilonok.playlistmaker.library.presentation.FavoritesViewModel
import com.ejilonok.playlistmaker.main.ui.common.BindingFragment
import com.ejilonok.playlistmaker.main.ui.common.*
import com.ejilonok.playlistmaker.player.ui.PlayerFragment
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.ejilonok.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private val viewModel: FavoritesViewModel by viewModel()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerFavoriteTrackList.adapter = TrackAdapter {track ->
            viewModel.trackClickDebounce(track)
        }

        viewModel.getStateLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoritesState.Loading -> renderLoading()
                is FavoritesState.Content -> renderContent(state.tracks)
                else -> renderEmptyState()
            }
        }

        viewModel.actionLiveData().observe(viewLifecycleOwner) { action ->
            when (action) {
                is FavoritesAction.GotoPlayerAction -> {
                    findNavController().navigate(
                    R.id.action_libraryFragment_to_playerFragment,
                    PlayerFragment.createArgs(action.track))}
                else -> {}
            }
        }

        viewModel.updateList()
    }

    private fun renderEmptyState() {
        binding.libraryEmpty.show()
        binding.libraryProgressBar.gone()
        binding.recyclerFavoriteTrackList.gone()
    }

    private fun renderLoading() {
        binding.libraryEmpty.gone()
        binding.libraryProgressBar.show()
        binding.recyclerFavoriteTrackList.gone()
    }

    private fun renderContent(tracks : List<Track>) {
        binding.libraryEmpty.gone()
        binding.libraryProgressBar.gone()

        (binding.recyclerFavoriteTrackList.adapter as TrackAdapter).setItems(tracks)
        binding.recyclerFavoriteTrackList.show()
    }
}
