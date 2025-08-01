package com.ejilonok.playlistmaker.library.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejilonok.playlistmaker.library.domain.api.interactor.FavoriteTrackInteractor
import com.ejilonok.playlistmaker.main.presentation.common.SingleLiveEvent
import com.ejilonok.playlistmaker.main.presentation.common.debounce
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.ejilonok.playlistmaker.search.presentation.SearchViewModel
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoriteInteractor : FavoriteTrackInteractor
) : ViewModel() {
    private var state : MutableLiveData<FavoritesState> = MutableLiveData(FavoritesState.Loading)
    fun getStateLiveData() : LiveData<FavoritesState> = state

    val trackClickDebounce: (Track) -> Unit

    init {
        trackClickDebounce =
            debounce(SearchViewModel.CLICK_DEBOUNCE_DELAY, viewModelScope, false) { track ->
                startPlayer(track)
            }
    }

    fun updateList() {
        viewModelScope.launch {
            favoriteInteractor.favoriteTracks().collect { tracks ->
                if (tracks.isEmpty())
                    state.postValue(FavoritesState.Empty)
                else
                    state.postValue(FavoritesState.Content(tracks))
            }
        }
    }

    private var action : SingleLiveEvent<FavoritesAction> = SingleLiveEvent(null)
    fun actionLiveData() : LiveData<FavoritesAction> = action
    private fun startPlayer(track: Track) {
        action.postValue(FavoritesAction.GotoPlayerAction(track))
    }
}
