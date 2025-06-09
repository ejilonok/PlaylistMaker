package com.ejilonok.playlistmaker.search.presentation

import com.ejilonok.playlistmaker.search.domain.models.Track

data class CommonState (
    var canClearSearch : Boolean = false,
    var hasFocus : Boolean = false,
    var searchText : String = "",
    var showKeyboard : Boolean = false)

sealed class SearchUiState {
    data object Waiting : SearchUiState()
    data class Content(val tracks : List<Track>) : SearchUiState()
    data class History(val tracks: List<Track>) : SearchUiState()
    data object Loading : SearchUiState()
    data object ServerError : SearchUiState()
    data object EmptySearchResult : SearchUiState()
}

data class SearchScreenState (
    val common : CommonState,
    val state : SearchUiState
)
