package com.ejilonok.playlistmaker.search.ui

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.databinding.FragmentSearchBinding
import com.ejilonok.playlistmaker.main.ui.common.*
import com.ejilonok.playlistmaker.player.ui.PlayerFragment
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.ejilonok.playlistmaker.search.presentation.SearchViewModel
import com.ejilonok.playlistmaker.search.presentation.SearchUiState
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : BindingFragment<FragmentSearchBinding>() {
    private var textWatcher : TextWatcher? = null
    private val searchViewModel: SearchViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        searchViewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { screenState ->
            when (screenState.state) {
                is SearchUiState.Content -> showSearchResult(screenState.state.tracks)
                is SearchUiState.History -> showHistory(screenState.state.tracks)
                SearchUiState.Waiting -> showEmptyScreen()
                SearchUiState.Loading -> showLoading()
                SearchUiState.EmptySearchResult -> showEmptySearchResult()
                SearchUiState.ServerError -> showServerError()
                is SearchUiState.GoToPlayer -> findNavController().navigate(
                    R.id.action_searchFragment_to_playerFragment,
                    PlayerFragment.createArgs(screenState.state.track))
            }

            binding.clearButton.setVisible(screenState.common.canClearSearch)

            if (binding.searchLine.text.toString() != screenState.common.searchText)
                binding.searchLine.setText( screenState.common.searchText )

            if (!screenState.common.showKeyboard)
                hideKeyboard()

            if (screenState.common.hasFocus)
                binding.searchLine.requestFocus()
            else
                binding.searchLine.clearFocus()
        }
    }

    override fun onDestroyView() {
        textWatcher?.let { binding.searchLine.removeTextChangedListener(it) }
        super.onDestroyView()
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
            searchViewModel.setHasFocus(hasFocus)
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
        val activity = requireActivity()
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
}
