package com.ejilonok.playlistmaker.library.ui

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.databinding.FragmentPlaylistMakerBinding
import com.ejilonok.playlistmaker.library.domain.models.Playlist
import com.ejilonok.playlistmaker.library.presentation.playlists.PlaylistMakerAction
import com.ejilonok.playlistmaker.library.presentation.playlists.PlaylistMakerState
import com.ejilonok.playlistmaker.library.presentation.playlists.PlaylistMakerViewModel
import com.ejilonok.playlistmaker.main.ui.common.BindingFragment
import com.ejilonok.playlistmaker.main.ui.common.DebouncingTextWatcher
import com.ejilonok.playlistmaker.main.ui.common.GraphicUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistMakerFragment : BindingFragment<FragmentPlaylistMakerBinding>() {
    private val viewModel: PlaylistMakerViewModel by viewModel()

    private var titleTextWatcher: TextWatcher? = null
    private var descriptionTextWatcher: TextWatcher? = null

    companion object {
        fun newInstance() = PlaylistMakerFragment()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistMakerBinding {
        return FragmentPlaylistMakerBinding.inflate(inflater, container, false)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.navigateBack()
        }
    }

    private fun handleState() = lifecycleScope.launch {
        viewModel.uiState.collect { state ->
            when (state) {
                is PlaylistMakerState.Empty -> binding.createPlaylist.isEnabled = false
                is PlaylistMakerState.CanSave -> {
                    binding.createPlaylist.isEnabled = true

                    bindingPlaylist(state.playlist)
                }

                is PlaylistMakerState.NoTitle -> {
                    binding.createPlaylist.isEnabled = false

                    bindingPlaylist(state.playlist)
                }
            }
        }
    }

    private fun handleAction() = lifecycleScope.launch {
        viewModel.action.collectLatest { action ->
            when (action) {
                is PlaylistMakerAction.GoBack -> {
                    findNavController().navigateUp()
                }
                is PlaylistMakerAction.SelectCover -> {
                    //todo проверить права и возможно открыть диалоговое окно на запрос прав на доступ к изображениям
                }
            }
        }
    }

    private fun bindingPlaylist(playlist: Playlist) {
        Glide.with(binding.root)
            .load(playlist.coverFilename)
            .placeholder(R.drawable.playlist_cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(GraphicUtils.dpToPx(8.0f, binding.root)))
            .into(binding.newPlaylistCover)

        if (binding.playlistTitleEditText.text.toString() != playlist.title) {
            binding.playlistTitleEditText.setText(playlist.title)
        }

        if (binding.playlistDescrEditText.text.toString() != playlist.description) {
            binding.playlistDescrEditText.setText(playlist.description)
        }
    }

    private fun setupTitleUi() {
        titleTextWatcher =
            DebouncingTextWatcher(viewModel.viewModelScope) { string -> viewModel.updateTitle(string) }
    }

    private fun setupDescriptionUi() {
        descriptionTextWatcher = DebouncingTextWatcher(viewModel.viewModelScope) { string ->
            viewModel.updateDescription(string)
        }
    }

    private fun setupCoverUi() {
        binding.newPlaylistCover.setOnClickListener {
            viewModel.selectCover()
        }
    }

    private fun setupBackButton() {
        binding.playlistBackButton.setOnClickListener {
            viewModel.navigateBack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTitleUi()
        setupDescriptionUi()
        setupCoverUi()
        setupBackButton()
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, backPressedCallback)

        handleState()
        handleAction()
    }
}