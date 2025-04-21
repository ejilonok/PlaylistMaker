package com.ejilonok.playlistmaker.search.presentation

import com.ejilonok.playlistmaker.search.domain.models.Track

sealed interface SearchState {
    object EmptyScreen : SearchState
    object Loading : SearchState
    object ServerError : SearchState
    object EmptySearchResult : SearchState
    data class Content (val tracks : List<Track>) : SearchState
    data class History (val tracks : List<Track>) : SearchState

}