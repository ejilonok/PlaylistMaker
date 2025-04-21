package com.ejilonok.playlistmaker.search.presentation

import android.app.Application
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.main.domain.consumer.ConsumerData
import com.ejilonok.playlistmaker.main.presentation.common.ClickDebouncer
import com.ejilonok.playlistmaker.main.presentation.common.SingleLiveEvent
import com.ejilonok.playlistmaker.main.presentation.common.TextInputDebouncer
import com.ejilonok.playlistmaker.search.domain.api.interactor.SearchHistoryInteractor
import com.ejilonok.playlistmaker.search.domain.models.Track

class SearchViewModel(
    application: Application
)  : AndroidViewModel(application) {
    private val tracksInteractor = Creator.provideTracksInteractor(getApplication())
    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor(getApplication())
    private val navigator = Creator.provideNavigator(getApplication())
    private var lastSearchText: String = ""
    private val clickDebouncer = ClickDebouncer(CLICK_DEBOUNCE_DELAY)
    private val searchDebounce = TextInputDebouncer({ startSearchTracks() }, SEARCH_DEBOUNCE_DELAY )
    private var lastSearchResult = listOf<Track>()

    private var screenState = MutableLiveData<SearchState>(SearchState.EmptyScreen)
    fun getScreenStateLiveData() : LiveData<SearchState> = screenState
    init {
        val history = searchHistoryInteractor.load()
        if (!searchHistoryInteractor.isHistoryEmpty())
            screenState.postValue(SearchState.History(history))
    }

    private var canClearSearchLine = MutableLiveData(false)
    fun getCanClearSearchLine() : LiveData<Boolean> = canClearSearchLine

    private var searchString = MutableLiveData("")
    fun searchStringLiveData() : LiveData<String> = searchString
    fun onSearchStringChanged(s : CharSequence?) {
        val text = s?.toString() ?: ""
        if (searchString.value != text) {
            searchString.postValue(text)
            canClearSearchLine.postValue(text.isNotEmpty())
            if (text.isEmpty()) {
                showHistory()
                searchDebounce.stop()
            } else {
                screenState.postValue(SearchState.EmptyScreen)
                searchDebounce.execute()
            }
        }
    }

    private var searchLineHasFocus = MutableLiveData(false)
    var searchLineHasFocusLiveData : LiveData<Boolean> = searchLineHasFocus
    fun searchFocusChangeListener(hasFocus : Boolean) {
        if (hasFocus) {
            showHistory()
        } else {
            renderState(SearchState.EmptyScreen)
        }
        if (hasFocus != searchLineHasFocus.value) {
            searchLineHasFocus.postValue(hasFocus)
        }
    }

    private var keyboardHide = SingleLiveEvent(Unit)
    fun getKeyboardVisibility() : LiveData<Unit> = keyboardHide

    override fun onCleared() {
        clickDebouncer.clearCalls()
        searchDebounce.onDestroy()
    }

    private fun renderState(state: SearchState) {
        screenState.postValue(state)
    }

    fun onSearchEditorAction(actionId : Int) : Boolean {
        return if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            keyboardHide.postValue(Unit)
            true
        }
        else false
    }

    fun onClickClearButton() {
        searchDebounce.stop()
        onSearchStringChanged("")
        searchLineHasFocus.postValue(false)
        canClearSearchLine.postValue(false)
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

    private fun startSearchTracks() {
        if (!tracksInteractor.isNetworkConnected()) {
            renderState(SearchState.ServerError)
            return
        }

        if ((lastSearchText == searchString.value) && (lastSearchResult.isNotEmpty())) {
            renderState(SearchState.Content(lastSearchResult))
            return
        }

        renderState(SearchState.Loading)

        tracksInteractor.searchTracks(searchString.value ?: "") { data -> getSearchResults(data) }

        lastSearchText = searchString.value ?: ""
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
            navigator.gotoPlayer(track)
        }
    }
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 600L
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(this[APPLICATION_KEY] as Application)
                }
            }
    }
}