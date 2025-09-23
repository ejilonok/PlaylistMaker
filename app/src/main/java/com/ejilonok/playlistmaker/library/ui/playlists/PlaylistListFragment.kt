package com.ejilonok.playlistmaker.library.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.databinding.FragmentPlaylistListBinding
import com.ejilonok.playlistmaker.library.presentation.playlists.PlaylistListAction
import com.ejilonok.playlistmaker.library.presentation.playlists.PlaylistListViewModel
import com.ejilonok.playlistmaker.main.ui.common.BindingFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistListFragment : BindingFragment<FragmentPlaylistListBinding>() {

    /* Оставляю для будущей бизнес-логики*/
    private val viewModel: PlaylistListViewModel by viewModel()

    companion object {
        fun newInstance() = PlaylistListFragment()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistListBinding {
        return FragmentPlaylistListBinding.inflate(inflater, container, false)
    }
    private fun handleActions() = lifecycleScope.launch {
        viewModel.action.collectLatest { action ->
            when (action) {
                is PlaylistListAction.CreateNewPlaylist -> findNavController().navigate(
                    R.id.action_libraryFragment_to_playlistMakerFragment)
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.recyclerPlaylistList.adapter = PlaylistAdapter { playlist ->
//            searchViewModel.historyClickDebouncer(track)
//        }

        binding.addPlaylist.setOnClickListener {
            viewModel.onCreateButtonClicked()
        }

        handleActions()
    }

}
