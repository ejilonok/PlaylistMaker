package com.ejilonok.playlistmaker.library.presentation.playlists

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejilonok.playlistmaker.library.domain.api.interactor.CoverInteractor
import com.ejilonok.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistMakerViewModel(
    private val coverInteractor: CoverInteractor,
) : ViewModel() {
    private var screenState = MutableStateFlow<PlaylistMakerState>(PlaylistMakerState.Empty)
    val uiState: StateFlow<PlaylistMakerState> = screenState.asStateFlow()

    private var _actions = MutableSharedFlow<PlaylistMakerAction>()
    val action: SharedFlow<PlaylistMakerAction> = _actions.asSharedFlow()

    fun updateTitle(newTitle: String) {
        val newTrimedTitle = newTitle.trim()

        when (screenState.value) {
            is PlaylistMakerState.Empty -> {
                if (newTrimedTitle.isNotEmpty()) screenState.value =
                    PlaylistMakerState.CanSave(Playlist(title = newTrimedTitle))
            }

            is PlaylistMakerState.NoTitle -> {
                if (newTrimedTitle.isNotEmpty()) screenState.value = PlaylistMakerState.CanSave(
                    (screenState.value as PlaylistMakerState.NoTitle).playlist.copy(title = newTrimedTitle)
                )
            }

            is PlaylistMakerState.CanSave -> {
                val newPlaylist =
                    (screenState.value as PlaylistMakerState.CanSave).playlist.copy(title = newTrimedTitle)
                screenState.value =
                    if (newTrimedTitle.isEmpty()) {
                        PlaylistMakerState.NoTitle(newPlaylist)
                    } else {
                        PlaylistMakerState.CanSave(newPlaylist)
                    }
            }
        }
    }

    fun getStateByPlaylist(playlist: Playlist): PlaylistMakerState {
        return if (playlist.title.isEmpty() && playlist.description.isEmpty() && playlist.coverFilename.isEmpty())
            PlaylistMakerState.Empty
        else {
            if (playlist.title.isEmpty())
                PlaylistMakerState.NoTitle(playlist)
            else PlaylistMakerState.CanSave(playlist)
        }
    }

    fun updateDescription(newDescription: String) {
        val newTrimmedDescription = newDescription.trim()

        when (screenState.value) {
            is PlaylistMakerState.Empty -> {
                if (newTrimmedDescription.isNotEmpty()) screenState.value =
                    PlaylistMakerState.NoTitle(
                        Playlist(
                            title = "",
                            description = newTrimmedDescription
                        )
                    )
            }

            is PlaylistMakerState.NoTitle -> {
                val newPlaylist =
                    (screenState.value as PlaylistMakerState.NoTitle).playlist.copy(description = newTrimmedDescription)
                screenState.value = getStateByPlaylist(newPlaylist)
            }

            is PlaylistMakerState.CanSave -> {
                val newPlaylist =
                    (screenState.value as PlaylistMakerState.NoTitle).playlist.copy(description = newTrimmedDescription)
                screenState.value = PlaylistMakerState.CanSave(newPlaylist)
            }
        }
    }

    fun saveCover(newCoverUri: Uri) {
        val oldCover = when (screenState.value) {
            is PlaylistMakerState.NoTitle -> (screenState.value as PlaylistMakerState.NoTitle).playlist.coverFilename
            is PlaylistMakerState.CanSave -> (screenState.value as PlaylistMakerState.CanSave).playlist.coverFilename
            else -> ""
        }

        if (oldCover.isNotEmpty()) {
            // удаляем файл из хранилища
            coverInteractor.deleteCover(oldCover)
        }

        // сохраняем файл в хранилище
        coverInteractor.saveCover(newCoverUri)
    }

    fun navigateBack() = viewModelScope.launch {
        _actions.emit(PlaylistMakerAction.GoBack)
    }

    fun selectCover() = viewModelScope.launch {
        _actions.emit(PlaylistMakerAction.SelectCover)
    }
}