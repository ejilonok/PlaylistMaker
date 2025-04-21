package com.ejilonok.playlistmaker.search.ui

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ejilonok.playlistmaker.databinding.ActivitySearchBinding
import com.ejilonok.playlistmaker.main.ui.common.gone
import com.ejilonok.playlistmaker.main.ui.common.setVisible
import com.ejilonok.playlistmaker.main.ui.common.show
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.ejilonok.playlistmaker.search.presentation.SearchViewModel
import com.ejilonok.playlistmaker.search.presentation.SearchState


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var textWatcher : TextWatcher? = null
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)

        searchViewModel = ViewModelProvider(this,
            SearchViewModel.getViewModelFactory())[SearchViewModel::class.java]

        setContentView(binding.root)

        binding.recyclerTrackList.adapter = TrackAdapter { track ->
            searchViewModel.addTrackAndStartPlayer(track)
        }
        binding.recyclerHistoryList.adapter = TrackAdapter {track ->
            searchViewModel.startPlayer(track)
        }

        setupClearHistoryButton()
        setupSearchLine()
        setupClearButton()
        setupUpdateButton()

        binding.searchBackButton.setOnClickListener { finish() }

        searchViewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is SearchState.Content -> showSearchResult(screenState.tracks)
                is SearchState.History -> showHistory(screenState.tracks)
                SearchState.EmptyScreen -> showEmptyScreen()
                SearchState.Loading -> showLoading()
                SearchState.EmptySearchResult -> showEmptySearchResult()
                SearchState.ServerError -> showServerError()
            }
        }
        searchViewModel.getCanClearSearchLine().observe(this) {canClear ->
            binding.clearButton.setVisible(canClear)
        }
        searchViewModel.searchStringLiveData().observe(this) { searchLine ->
            // это условие, чтобы не скидывалось положение курсора при вводе текста в поле
            if (searchLine != binding.searchLine.text.toString())
                binding.searchLine.setText(searchLine)
        }

        searchViewModel.getKeyboardVisibility().observe(this) {
            hideKeyboard()
        }

        searchViewModel.searchLineHasFocusLiveData.observe(this) {
            if (it) {
                binding.searchLine.requestFocus()
            } else {
                binding.searchLine.clearFocus()
            }
        }
    }

    override fun onDestroy() {
        textWatcher?.let { binding.searchLine.removeTextChangedListener(it) }
        super.onDestroy()
    }

    private fun setupUpdateButton() {
        binding.updateButton.setOnClickListener {
            searchViewModel.updateSearch()
        }
    }

    private fun setupClearHistoryButton() {
        binding.clearHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
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
                searchViewModel.onSearchStringChanged(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        searchLine.addTextChangedListener(textWatcher)
        searchLine.setOnFocusChangeListener { _, hasFocus ->
            searchViewModel.searchFocusChangeListener(hasFocus)
        }

        searchLine.setOnEditorActionListener { _, actionId, _ ->
            searchViewModel.onSearchEditorAction(actionId)
        }
    }

    private fun setupClearButton() {
        binding.clearButton.setOnClickListener {
            searchViewModel.onClickClearButton()
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
        (binding.recyclerTrackList.adapter as TrackAdapter).setItems(tracks)
        showSearchResult()
    }

    private fun showSearchResult() {
        hideHistory()
        hideSearchResults()
        hideProgressBar()

        binding.recyclerTrackList.show()
    }

    private fun showServerError() {
        hideHistory()
        hideSearchResults()
        binding.serverError.show()
        binding.updateButton.show()
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

    private fun showLoading() {
        hideSearchResults()
        hideHistory()
        showProgressBar()
    }

    private fun hideHistory() {
        binding.historyGroup.gone()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}