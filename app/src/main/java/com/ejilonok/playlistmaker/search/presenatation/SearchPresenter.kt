package com.ejilonok.playlistmaker.search.presenatation

import android.content.Intent
import android.view.inputmethod.EditorInfo
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.main.PlaylistMakerApplication
import com.ejilonok.playlistmaker.main.domain.consumer.ConsumerData
import com.ejilonok.playlistmaker.main.ui.common.ClickDebouncer
import com.ejilonok.playlistmaker.main.ui.common.TextInputDebouncer
import com.ejilonok.playlistmaker.player.ui.PlayerActivity
import com.ejilonok.playlistmaker.search.domain.api.interactor.SearchHistoryInteractor
import com.ejilonok.playlistmaker.search.domain.api.interactor.TrackInteractor
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.ejilonok.playlistmaker.search.ui.SearchActivity

class SearchPresenter(
    private val searchActivity: SearchActivity,
    private val searchView: SearchView
) {
    private val tracksInteractor: TrackInteractor by lazy { Creator.provideTracksInteractor(searchActivity) }
    private var searchString : String = ""
    private val searchHistoryInteractor : SearchHistoryInteractor by lazy { Creator.provideSearchHistoryInteractor(searchActivity.applicationContext) }
    private var lastSearchText: String = ""
    private val clickDebouncer = ClickDebouncer(CLICK_DEBOUNCE_DELAY)
    private val searchDebounce = TextInputDebouncer({ startSearchTracks() }, SEARCH_DEBOUNCE_DELAY )

    fun onCreate() {
        searchView.setSearchLineText(searchString)
        showSearchResults()
    }

    fun onDestroy() {
        clickDebouncer.onDestroy()
        searchDebounce.onDestroy()
    }

    private fun showSearchResults() {
        if (searchString.isEmpty()) {
            showHistory()
        } else {
            startSearchTracks()
        }
    }

    fun onSearchEditorAction(actionId : Int) : Boolean {
        return if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            searchView.hideKeyboard()
            true
        } else {
            false
        }
    }

    fun searchFocusChangeListener(hasFocus : Boolean) {
        if (hasFocus) {
            showHistory()
        } else {
            searchView.render(SearchState.EmptyScreen)
        }
    }

    fun onClickClearButton() {
        searchView.setSearchLineText("")
        searchView.showScreenWithoutFocus()
        searchView.setCanClearSearchLine(false)
        showHistory()
    }

    fun updateSearch() {
        lastSearchText = ""
        startSearchTracks()
    }

    private fun showHistory() {
        val history = searchHistoryInteractor.load()
        if (history.isEmpty()) {
            searchView.render(SearchState.EmptyScreen)
        } else {
            searchView.render(SearchState.History(history))
        }
    }

    fun searchTextChanged(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            searchView.setCanClearSearchLine(false)
            showHistory()
            searchDebounce.stop()
        } else {
            searchView.setCanClearSearchLine(true)
            searchString = s.toString()
            searchDebounce.execute()
        }
    }

    private fun startSearchTracks() {
        if (!tracksInteractor.isNetworkConnected()) {
            searchView.render(SearchState.ServerError)
            return
        }

        if (lastSearchText == searchString) {
            searchView.showSearchResult()
            return
        }

        searchView.render(SearchState.Loading)

        tracksInteractor.searchTracks(searchString) { data -> searchActivity.runOnUiThread {getSearchResults(data)} }

        lastSearchText = searchString
    }

    private fun getSearchResults(data: ConsumerData<List<Track>>) {
        when (data) {
            is ConsumerData.Data -> {
                if (data.data.isEmpty()) {
                    searchView.render(SearchState.EmptySearchResult)
                } else {
                    searchView.render(SearchState.Content(data.data))
                }
            }

            is ConsumerData.Error -> {
                searchView.render(SearchState.ServerError)
            }
        }
    }

    private fun addTrackToHistory(track: Track) {
        searchHistoryInteractor.addTrack(track , object : SearchHistoryInteractor.SearchHistoryConsumer {
            override fun consume(actualHistory: List<Track>) {
            }
        })
    }

    fun clearHistory() {
        searchHistoryInteractor.clear()
        showHistory()
    }

    fun addTrackAndStartPlayer(track: Track) {
        addTrackToHistory(track)
        startPlayer(track)
    }

    // TODO: перенести работу с вызовом активити в дата слой 
    fun startPlayer(track: Track) {
        (searchActivity.application as PlaylistMakerApplication).actualTrack = track
        if (clickDebouncer.can()) {
            val playerIntent = Intent(searchActivity, PlayerActivity::class.java)
            searchActivity.startActivity(playerIntent)
        }
    }
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 600L
    }
}