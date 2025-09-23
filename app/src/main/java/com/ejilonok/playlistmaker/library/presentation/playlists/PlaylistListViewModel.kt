package com.ejilonok.playlistmaker.library.presentation.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejilonok.playlistmaker.library.domain.api.interactor.PlaylistInteractor
import com.ejilonok.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistListViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private var screenState = MutableStateFlow<PlaylistListState>(PlaylistListState.Loading)
    val uiState: StateFlow<PlaylistListState> = screenState.asStateFlow()

    private var _actions = MutableSharedFlow<PlaylistListAction>()
    val action: SharedFlow<PlaylistListAction> = _actions.asSharedFlow()
    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            playlistInteractor.playlists()
                .collect {playlistList ->
                    bindList(playlistList)
                }
        }
    }

    private fun bindList(playlistList: List<Playlist>) {
        if (playlistList.isEmpty()) {
            screenState.update { PlaylistListState.Empty }
        } else {
            // bind в адаптер
            screenState.update { PlaylistListState.Content(playlistList) }
        }
    }

    fun onCreateButtonClicked() =
        viewModelScope.launch {
            _actions.emit(PlaylistListAction.CreateNewPlaylist)
        }

}
