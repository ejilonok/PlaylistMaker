package com.ejilonok.playlistmaker.search.presenatation

import com.ejilonok.playlistmaker.search.domain.models.Track

interface SearchView {
    fun render(state: SearchState)
    fun showEmptyScreen()
    fun showHistory(actualHistory: List<Track>)
    fun showScreenWithoutFocus()
    fun showLoading()
    fun showEmptySearchResult()
    fun showServerError()
    fun showSearchResult(tracks: List<Track>)
    fun showSearchResult()

    fun setCanClearSearchLine(canClean : Boolean)
    fun hideKeyboard()

    fun setSearchLineText(text : String)
}