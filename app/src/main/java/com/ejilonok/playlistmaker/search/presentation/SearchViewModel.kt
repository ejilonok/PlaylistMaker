package com.ejilonok.playlistmaker.search.presentation

import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejilonok.playlistmaker.main.domain.consumer.ConsumerData
import com.ejilonok.playlistmaker.main.presentation.common.SingleLiveEvent
import com.ejilonok.playlistmaker.main.presentation.common.debounce
import com.ejilonok.playlistmaker.search.domain.api.interactor.SearchHistoryInteractor
import com.ejilonok.playlistmaker.search.domain.api.interactor.TrackInteractor
import com.ejilonok.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TrackInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
)  : ViewModel() {
    private var searchDebounce: Job? = null

    val trackClickDebounce: (Track) -> Unit
    val historyClickDebouncer: (Track) -> Unit

    private var lastSearchResult = listOf<Track>()
    private var lastSearchText: String = ""

    private var screenState = MutableLiveData(SearchScreenState(CommonState(), SearchUiState.Waiting))
    init {
        trackClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewModelScope, false) {track ->
            startPlayer(track)
        }

        historyClickDebouncer = debounce(CLICK_DEBOUNCE_DELAY, viewModelScope, false) {track ->
            addTrackAndStartPlayer(track)
        }
    }
    fun getScreenStateLiveData() : LiveData<SearchScreenState> = screenState

    private var gotoPlayerAction : SingleLiveEvent<Track> = SingleLiveEvent(null)

    fun gotoPlayerLiveData() : LiveData<Track> = gotoPlayerAction
    private fun getSearchText() : String = screenState.value?.common?.searchText ?: ""
    fun setHasFocus(hasFocus: Boolean) {
        if (hasFocus && (screenState.value?.common?.searchText?.isEmpty() != false)) {
            postState(getCommonState().copy( hasFocus = true , showKeyboard = true ), needShowHistory())
        }
    }

    fun onSearchStringChanged(s : CharSequence?) {
        val text = s?.toString() ?: ""
        screenState.value?.let { state ->
            if (state.common.searchText != text) {
                if (text.isEmpty()) {
                    postState(state.common.copy(searchText = text, canClearSearch = text.isNotEmpty()), needShowHistory())
                    searchDebounce?.cancel()
                } else {
                    postState(getCommonState().copy(searchText = text, canClearSearch = text.isNotEmpty()), SearchUiState.Waiting)
                    searchDebounce = viewModelScope.launch {
                        delay(SEARCH_DEBOUNCE_DELAY)
                        startSearchTracks()
                    }
                }
            }
        }
    }

    private fun getCommonState() : CommonState {
        return screenState.value?.common ?: CommonState()
    }
    private fun postState(common : CommonState, state : SearchUiState) {
        Log.d("postState", "$common - $state")
        screenState.postValue(
            screenState.value?.copy(
                common = common,
                state = state
            ) ?: SearchScreenState(common, state))
    }
    private fun postState(stateUi : SearchUiState) {
        postState(common = getCommonState().copy(), state = stateUi)
    }
    private fun postState(common : CommonState) {
        postState(common, screenState.value?.state ?: SearchUiState.Waiting)
    }

    fun onSearchEditorAction(actionId : Int) : Boolean {
        return if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            postState(getCommonState().copy(showKeyboard = false, hasFocus = false))
            updateSearch()
            true
        }
        else false
    }

    fun onClickClearButton() {
        searchDebounce?.cancel()
        // data класс осуществляет вызов observer's только в случае изменения значений,
        // поэтому можно вызвать конструктор класса - observer отработает только для измененных полей.
        postState(CommonState(), SearchUiState.Waiting)
    }

    fun updateSearch() {
        lastSearchText = ""
        searchDebounce?.cancel()
        searchDebounce = viewModelScope.launch {
            // в случае обновления отправляем задачу поиска композиций на немедленное исполнение
            startSearchTracks()
        }
    }

    private fun needShowHistory() : SearchUiState {
        val history = searchHistoryInteractor.load()

        return if (history.isNotEmpty()) SearchUiState.History(history) else SearchUiState.Waiting
    }

    private fun startSearchTracks() {
        if (!tracksInteractor.isNetworkConnected()) {
            postState(SearchUiState.ServerError)
            return
        }

        if ((lastSearchText == getSearchText()) && (lastSearchResult.isNotEmpty())) {
            postState(SearchUiState.Content(lastSearchResult))
            return
        }

        postState(SearchUiState.Loading)

        // Далее мы оформляем запрос на поиск в интерактор.
        // Сохраняем значение строки, чтобы сохранить по какому тексту выполнился последний запрос.
        // Даже если из другого потока изменится текст это не вызовет ошибку логики показа результатов последнего поиска
        val searchText = getSearchText()
        viewModelScope.launch {
            tracksInteractor
                .searchTracks(searchText)
                .collect { data -> getSearchResults(data) }
        }
        lastSearchText = searchText
    }

    private fun getSearchResults(data: ConsumerData<List<Track>>) {
        when (data) {
            is ConsumerData.Data -> {
                lastSearchResult = data.data
                if (data.data.isEmpty()) {
                    postState(SearchUiState.EmptySearchResult)
                } else {
                    postState(SearchUiState.Content(data.data))
                }
            }

            is ConsumerData.Error -> {
                postState(SearchUiState.ServerError)
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
        postState(needShowHistory())
    }

    private fun addTrackAndStartPlayer(track: Track) {
        addTrackToHistory(track)
        startPlayer(track)
    }

    private fun startPlayer(track: Track) {
        gotoPlayerAction.postValue(track)
    }
    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 150L
    }
}
