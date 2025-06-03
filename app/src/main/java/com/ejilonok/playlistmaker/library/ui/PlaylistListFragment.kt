package com.ejilonok.playlistmaker.library.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ejilonok.playlistmaker.databinding.FragmentPlaylistListBinding
import com.ejilonok.playlistmaker.library.presentation.PlaylistListViewModel
import com.ejilonok.playlistmaker.main.ui.common.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistListFragment : BindingFragment<FragmentPlaylistListBinding>() {

    companion object {
        fun newInstance() = PlaylistListFragment()
    }

    /* Оставляю для будущей бизнес-логики*/
    private val viewModel: PlaylistListViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistListBinding {
        return FragmentPlaylistListBinding.inflate(inflater, container, false)
    }

}
