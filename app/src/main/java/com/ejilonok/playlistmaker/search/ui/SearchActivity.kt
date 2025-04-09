package com.ejilonok.playlistmaker.search.ui

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.databinding.ActivitySearchBinding
import com.ejilonok.playlistmaker.main.ui.common.gone
import com.ejilonok.playlistmaker.main.ui.common.show
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.ejilonok.playlistmaker.search.presenatation.SearchPresenter
import com.ejilonok.playlistmaker.search.presenatation.SearchState
import com.ejilonok.playlistmaker.search.presenatation.SearchView


class SearchActivity : AppCompatActivity(), SearchView {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchPresenter: SearchPresenter
    private var textWatcher : TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)
        searchPresenter = Creator.provideSearchPresenter(this,this)

        binding.recyclerTrackList.adapter = TrackAdapter { track ->
            searchPresenter.addTrackAndStartPlayer(track)
        }
        binding.recyclerHistoryList.adapter = TrackAdapter {track ->
            searchPresenter.startPlayer(track)
        }

        setupClearHistoryButton()
        setupSearchLine()
        setupClearButton()
        setupUpdateButton()

        binding.searchBackButton.setOnClickListener { finish() }
        searchPresenter.onCreate()
    }

    override fun onDestroy() {
        searchPresenter.onDestroy()
        textWatcher?.let { binding.searchLine.removeTextChangedListener(it) }
        super.onDestroy()
    }

    private fun setupUpdateButton() {
        binding.updateButton.setOnClickListener {
            searchPresenter.updateSearch()
        }
    }

    private fun setupClearHistoryButton() {
        binding.clearHistoryButton.setOnClickListener {
            searchPresenter.clearHistory()
        }
    }

    private fun setupSearchLine() {
        val searchLine = binding.searchLine

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchPresenter.searchTextChanged(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        searchLine.addTextChangedListener(textWatcher)
        searchLine.setOnFocusChangeListener { _, hasFocus ->
            searchPresenter.searchFocusChangeListener(hasFocus)
        }

        searchLine.requestFocus()
        searchLine.setOnEditorActionListener { _, actionId, _ ->
            searchPresenter.onSearchEditorAction(actionId)
        }
    }

    private fun setupClearButton() {
        binding.clearButton.setOnClickListener {
            searchPresenter.onClickClearButton()
        }
    }
    override fun setSearchLineText(text : String) {
        binding.searchLine.setText(text)
    }

    override fun render(state: SearchState) {
        when (state) {
            is SearchState.Content -> showSearchResult(state.tracks)
            is SearchState.History -> showHistory(state.tracks)
            SearchState.EmptyScreen -> showEmptyScreen()
            SearchState.Loading -> showLoading()
            SearchState.EmptySearchResult -> showEmptySearchResult()
            SearchState.ServerError -> showServerError()
        }
    }

    private fun showEmptyScreen() {
        hideHistory()
        hideSearchResults()
        hideProgressBar()
    }

    private fun showProgressBar() {
        binding.searchProgressBar.show()
    }

    private fun hideProgressBar() {
        binding.searchProgressBar.gone()
    }

    private fun showSearchResult(tracks: List<Track>) {
        setTrackList(tracks)
        showSearchResult()
    }

    override fun showSearchResult() {
        hideHistory()
        hideSearchResults()
        hideProgressBar()

        val recyclerView = binding.recyclerTrackList
        recyclerView.show()
        recyclerView.scrollToPosition(0)
    }

    private fun showServerError() {
        hideHistory()
        hideSearchResults()
        binding.serverError.show()
        binding.updateButton.show()
    }

    override fun setCanClearSearchLine(canClean: Boolean) {
        if (canClean) {
            binding.clearButton.show()
        } else {
            binding.clearButton.gone()
        }
    }

    private fun showEmptySearchResult() {
        hideHistory()
        hideSearchResults()
        binding.searchError.show()
    }

    private fun hideSearchResults() {
        binding.searchError.gone()
        binding.serverError.gone()
        binding.recyclerTrackList.gone()
        binding.updateButton.gone()
        binding.searchProgressBar.gone()
    }

    private fun showHistory(actualHistory: List<Track>) {
        hideSearchResults()
        hideProgressBar()
        (binding.recyclerHistoryList.adapter as TrackAdapter).setItems(actualHistory)
        binding.historyGroup.show()
        binding.recyclerHistoryList.scrollToPosition(0)
    }

    override fun showScreenWithoutFocus() {
        showEmptyScreen()
        binding.searchLine.clearFocus()
        hideKeyboard()
    }

    private fun showLoading() {
        hideSearchResults()
        hideHistory()
        showProgressBar()
    }

    private fun hideHistory() {
        binding.historyGroup.gone()
    }

    override fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }


    private fun setTrackList(tracks: List<Track>) {
        (binding.recyclerTrackList.adapter as TrackAdapter).setItems(tracks)
    }
}