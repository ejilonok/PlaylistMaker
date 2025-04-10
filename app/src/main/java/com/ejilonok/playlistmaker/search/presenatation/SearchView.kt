package com.ejilonok.playlistmaker.search.presenatation

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface SearchView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: SearchState)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showScreenWithoutFocus()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSearchResult()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setCanClearSearchLine(canClean : Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun hideKeyboard()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSearchLineText(text : String)
}