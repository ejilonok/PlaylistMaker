package com.ejilonok.playlistmaker.search.presenatation

import com.ejilonok.playlistmaker.search.domain.models.Track

interface SearchView {
    fun render(state: SearchState)
    fun showScreenWithoutFocus()
    fun showSearchResult()

    fun setCanClearSearchLine(canClean : Boolean)
    fun hideKeyboard()

    fun setSearchLineText(text : String)
}