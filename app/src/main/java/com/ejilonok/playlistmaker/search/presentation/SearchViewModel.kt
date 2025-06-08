package com.ejilonok.playlistmaker.search.presentation

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ejilonok.playlistmaker.main.domain.consumer.ConsumerData
import com.ejilonok.playlistmaker.main.presentation.common.ClickDebouncer
import com.ejilonok.playlistmaker.main.presentation.common.TextInputDebouncer
import com.ejilonok.playlistmaker.search.domain.api.interactor.SearchHistoryInteractor
import com.ejilonok.playlistmaker.search.domain.api.interactor.TrackInteractor
import com.ejilonok.playlistmaker.search.domain.models.Track

class SearchViewModel(
    private val tracksInteractor: TrackInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val clickDebouncer: ClickDebouncer,
    private val searchDebounce: TextInputDebouncer
)  : ViewModel() {
    private var lastSearchResult = listOf<Track>()
    private var lastSearchText: String = ""

    private var screenState = MutableLiveData(SearchScreenState(CommonState(), SearchUiState.Waiting))
    init {
        searchDebounce.runnable = Runnable(this::startSearchTracks)
    }
    fun getScreenStateLiveData() : LiveData<SearchScreenState> = screenState
    private fun getSearchText() : String = screenState.value?.common?.searchText ?: ""
    fun setHasFocus(hasFocus: Boolean) {
        if (hasFocus) {
            postState(getCommonState().copy( hasFocus = true , showKeyboard = true ), needShowHistory())
        } else {
            postState(getCommonState().copy( hasFocus = false ), SearchUiState.Waiting)
        }
    }

    fun onSearchStringChanged(s : CharSequence?) {
        val text = s?.toString() ?: ""
        screenState.value?.let { state ->
            if (state.common.searchText != text) {
                if (text.isEmpty()) {
                    postState(state.common.copy(searchText = text, canClearSearch = text.isNotEmpty()), needShowHistory())
                    searchDebounce.stop()
                } else {
                    postState(getCommonState().copy(searchText = text, canClearSearch = text.isNotEmpty()), SearchUiState.Waiting)
                    searchDebounce.execute()
                }
            }
        }
    }

    private fun getCommonState() : CommonState {
        return screenState.value?.common ?: CommonState()
    }
    private fun postState(common : CommonState, state : SearchUiState) {
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

    override fun onCleared() {
        clickDebouncer.clearCalls()
        searchDebounce.onDestroy()
    }

    fun onSearchEditorAction(actionId : Int) : Boolean {
        return if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            postState(getCommonState().copy(showKeyboard = false))
            true
        }
        else false
    }

    fun onClickClearButton() {
        searchDebounce.stop()
        // data класс осуществляет вызов observer's только в случае изменения значений,
        // поэтому можно вызвать конструктор класса - observer отработает только для измененных полей.
        postState(CommonState(), SearchUiState.Waiting)
    }

    fun updateSearch() {
        lastSearchText = ""
        startSearchTracks()
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
        tracksInteractor.searchTracks(searchText) { data -> getSearchResults(data) }
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

    fun addTrackAndStartPlayer(track: Track) {
        addTrackToHistory(track)
        startPlayer(track)
    }

    fun startPlayer(track: Track) {
        if (clickDebouncer.can()) {
            postState(SearchUiState.GoToPlayer(track))
        }
    }
    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 600L
    }
}
