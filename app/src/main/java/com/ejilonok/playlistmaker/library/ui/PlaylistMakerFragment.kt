package com.ejilonok.playlistmaker.library.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ejilonok.playlistmaker.databinding.FragmentPlaylistMakerBinding
import com.ejilonok.playlistmaker.library.presentation.playlists.PlaylistMakerViewModel
import com.ejilonok.playlistmaker.main.ui.common.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistMakerFragment : BindingFragment<FragmentPlaylistMakerBinding>() {
    private val viewModel: PlaylistMakerViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistMakerBinding {
        return FragmentPlaylistMakerBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = PlaylistMakerFragment()
    }
}