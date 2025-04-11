package com.ejilonok.playlistmaker.search.presenatation


interface SearchView {
    fun render(state: SearchState)
    fun showScreenWithoutFocus()
    fun showSearchResult()

    fun setCanClearSearchLine(canClean : Boolean)
    fun hideKeyboard()
    fun setSearchLineText(text : String)
}