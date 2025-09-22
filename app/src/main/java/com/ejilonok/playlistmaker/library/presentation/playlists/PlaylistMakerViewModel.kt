package com.ejilonok.playlistmaker.library.presentation.playlists

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.ejilonok.playlistmaker.library.domain.api.interactor.CoverInteractor
import com.ejilonok.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlaylistMakerViewModel(
    private val coverInteractor: CoverInteractor,
) : ViewModel() {
    private var screenState = MutableStateFlow<PlaylistMakerState>(PlaylistMakerState.Empty)
    val uiState: StateFlow<PlaylistMakerState> = screenState.asStateFlow()

    fun updateTitle(newTitle: String) {
        val newTrimedTitle = newTitle.trim()

        when (screenState.value) {
            is PlaylistMakerState.Empty -> {
                if (!newTrimedTitle.isNullOrEmpty()) screenState.value =
                    PlaylistMakerState.CanSave(Playlist(title = newTrimedTitle))
            }

            is PlaylistMakerState.NoTitle -> {
                if (!newTrimedTitle.isNullOrEmpty()) screenState.value = PlaylistMakerState.CanSave(
                    (screenState.value as PlaylistMakerState.NoTitle).playlist.copy(title = newTrimedTitle)
                )
            }

            is PlaylistMakerState.CanSave -> {
                val newPlaylist =
                    (screenState.value as PlaylistMakerState.CanSave).playlist.copy(title = newTrimedTitle)
                screenState.value =
                    if (newTrimedTitle.isNullOrEmpty()) {
                        PlaylistMakerState.NoTitle(newPlaylist)
                    } else {
                        PlaylistMakerState.CanSave(newPlaylist)
                    }
            }
        }
    }

    fun getStateByPlaylist(playlist: Playlist): PlaylistMakerState {
        return if (playlist.title.isNullOrEmpty() && playlist.description.isNullOrEmpty() && playlist.coverFilename.isNullOrEmpty())
            PlaylistMakerState.Empty
        else {
            if (playlist.title.isNullOrEmpty())
                PlaylistMakerState.NoTitle(playlist)
            else PlaylistMakerState.CanSave(playlist)
        }
    }

    fun updateDescription(newDescription : String) {
        val newTrimedDescription = newDescription.trim()

        when (screenState.value) {
            is PlaylistMakerState.Empty -> {
                if (!newTrimedDescription.isNullOrEmpty()) screenState.value =
                    PlaylistMakerState.NoTitle(
                        Playlist(
                            title = "",
                            description = newTrimedDescription
                        )
                    )
            }

            is PlaylistMakerState.NoTitle -> {
                val newPlaylist = (screenState.value as PlaylistMakerState.NoTitle).playlist.copy(description = newTrimedDescription)
                screenState.value = getStateByPlaylist(newPlaylist)
            }

            is PlaylistMakerState.CanSave -> {
                val newPlaylist = (screenState.value as PlaylistMakerState.NoTitle).playlist.copy(description = newTrimedDescription)
                screenState.value = PlaylistMakerState.CanSave(newPlaylist)
            }
        }
    }

    fun saveCover(newCoverUri : Uri) {
        val oldCover = when (screenState.value) {
            is PlaylistMakerState.NoTitle -> (screenState.value as PlaylistMakerState.NoTitle).playlist.coverFilename
            is PlaylistMakerState.CanSave -> (screenState.value as PlaylistMakerState.CanSave).playlist.coverFilename
            else -> ""
        }
        
        if (!oldCover.isNullOrEmpty()) {
            // удаляем файл из хранилища
            coverInteractor.deleteCover(oldCover)
        }

        // сохраняем файл в хранилище
        coverInteractor.saveCover(newCoverUri)
    }
}