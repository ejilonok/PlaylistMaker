package com.ejilonok.playlistmaker.search.presenatation

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.main.domain.consumer.ConsumerData
import com.ejilonok.playlistmaker.main.ui.common.ClickDebouncer
import com.ejilonok.playlistmaker.main.ui.common.TextInputDebouncer
import com.ejilonok.playlistmaker.player.ui.PlayerActivity
import com.ejilonok.playlistmaker.search.domain.api.interactor.SearchHistoryInteractor
import com.ejilonok.playlistmaker.search.domain.api.interactor.TrackInteractor
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import moxy.MvpPresenter

class SearchPresenter(
    private val context: Context
)  : MvpPresenter<SearchView>() {
    private val tracksInteractor: TrackInteractor by lazy { Creator.provideTracksInteractor(context) }
    private var searchString : String = ""
    private val searchHistoryInteractor : SearchHistoryInteractor by lazy { Creator.provideSearchHistoryInteractor(context.applicationContext) }
    private var lastSearchText: String = ""
    private val clickDebouncer = ClickDebouncer(CLICK_DEBOUNCE_DELAY)
    private val searchDebounce = TextInputDebouncer({ startSearchTracks() }, SEARCH_DEBOUNCE_DELAY )
    private var lastSearchResult = listOf<Track>()

    override fun onDestroy() {
        clickDebouncer.onDestroy()
        searchDebounce.onDestroy()

        super.onDestroy()
    }

    private fun renderState(state: SearchState) {
        viewState.render(state)
    }

    fun onSearchEditorAction(actionId : Int) : Boolean {
        return if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            viewState.hideKeyboard()
            true
        } else {
            false
        }
    }

    fun searchFocusChangeListener(hasFocus : Boolean) {
        if (hasFocus) {
            showHistory()
        } else {
            renderState(SearchState.EmptyScreen)
        }
    }

    fun onClickClearButton() {
        viewState.setSearchLineText("")
        viewState.showScreenWithoutFocus()
        viewState.setCanClearSearchLine(false)
        showHistory()
    }

    fun updateSearch() {
        lastSearchText = ""
        startSearchTracks()
    }

    private fun showHistory() {
        val history = searchHistoryInteractor.load()
        if (history.isEmpty()) {
            renderState(SearchState.EmptyScreen)
        } else {
            renderState(SearchState.History(history))
        }
    }

    fun searchTextChanged(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            viewState.setCanClearSearchLine(false)
            showHistory()
            searchDebounce.stop()
        } else {
            viewState.setCanClearSearchLine(true)
            searchString = s.toString()
            searchDebounce.execute()
        }
    }

    private fun startSearchTracks() {
        if (!tracksInteractor.isNetworkConnected()) {
            renderState(SearchState.ServerError)
            return
        }

        if ((lastSearchText == searchString) && (lastSearchResult.isNotEmpty())) {
            renderState(SearchState.Content(lastSearchResult))
            return
        }

        renderState(SearchState.Loading)

        tracksInteractor.searchTracks(searchString) { data -> mainHandler.post {getSearchResults(data)} }

        lastSearchText = searchString
    }

    private fun getSearchResults(data: ConsumerData<List<Track>>) {
        when (data) {
            is ConsumerData.Data -> {
                lastSearchResult = data.data
                if (data.data.isEmpty()) {
                    renderState(SearchState.EmptySearchResult)
                } else {
                    renderState(SearchState.Content(data.data))
                }
            }

            is ConsumerData.Error -> {
                renderState(SearchState.ServerError)
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
        if (clickDebouncer.can()) {
            val playerIntent = Intent(context, PlayerActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK  // Добавляем флаг
                putExtra("TRACK_JSON", Gson().toJson(track))
            }
            context.startActivity(playerIntent)
        }
    }
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 600L
        private val mainHandler = Handler(Looper.getMainLooper())
    }
}