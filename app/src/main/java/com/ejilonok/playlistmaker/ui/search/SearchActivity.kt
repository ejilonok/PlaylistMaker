package com.ejilonok.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher

import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.PlaylistMakerApplication
import com.ejilonok.playlistmaker.databinding.ActivitySearchBinding
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.domain.api.interactors.SearchHistoryInteractor
import com.ejilonok.playlistmaker.domain.api.interactors.TrackInteractor
import com.ejilonok.playlistmaker.domain.consumer.ConsumerData
import com.ejilonok.playlistmaker.domain.models.Track
import com.ejilonok.playlistmaker.presentation.tracks.TrackAdapter
import com.ejilonok.playlistmaker.ui.common.ClickDebouncer
import com.ejilonok.playlistmaker.ui.common.TextInputDebouncer
import com.ejilonok.playlistmaker.ui.common.gone
import com.ejilonok.playlistmaker.ui.common.show
import com.ejilonok.playlistmaker.ui.player.PlayerActivity


class SearchActivity : AppCompatActivity() {
    private val tracksInteractor: TrackInteractor by lazy { Creator.provideTracksInteractor() }
    private val searchSettingsInteractor = Creator.provideSearchSettingsInteractor()

    private lateinit var binding: ActivitySearchBinding
    private val searchHistoryInteractor : SearchHistoryInteractor by lazy { Creator.provideSearchHistoryInteractor(applicationContext) }

    private var lastSearchText: String = ""

    private val clickDebouncer = ClickDebouncer(CLICK_DEBOUNCE_DELAY)
    private val searchDebounce = TextInputDebouncer({ startSearchTracks() }, SEARCH_DEBOUNCE_DELAY )

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 600L
        private const val SETTINGS_TAG = "SETTINGS"
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        outState.putString(SETTINGS_TAG, searchSettingsInteractor.save())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        searchSettingsInteractor.load(savedInstanceState.getString(SETTINGS_TAG))
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerTrackList.adapter = TrackAdapter (trackClickListener =  ::addTrackAndStartPlayer )
        binding.recyclerHistoryList.adapter = TrackAdapter(searchHistoryInteractor.load(), ::startPlayer)

        setupClearHistoryButton()
        setupSearchLine()
        setupClearButton()
        setupUpdateButton()

        binding.searchBackButton.setOnClickListener { this.finish() }

        showHistory()
    }

    private fun setupUpdateButton() {
        binding.updateButton.setOnClickListener {
            lastSearchText = ""
            startSearchTracks()
        }
    }

    private fun setupClearHistoryButton() {
        binding.clearHistoryButton.setOnClickListener {
            clearHistory()
        }
    }

    private fun setupSearchLine() {
        val searchLine = binding.searchLine
        searchLine.setText(searchSettingsInteractor.getSearchString())

        val clearTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.clearButton.gone()
                    hideSearchResults()
                    showHistory()
                    searchDebounce.stop()
                } else {
                    binding.clearButton.show()
                    hideHistory()
                    searchDebounce.execute()
                }

                searchSettingsInteractor.save(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        searchLine.addTextChangedListener(clearTextWatcher)
        searchLine.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchLine.text.isNullOrEmpty()) showHistory() else hideHistory()
        }

        searchLine.requestFocus()
        searchLine.setOnEditorActionListener { _, actionId, _ ->
            /* обработку события unspecified сделала для упрощения отладки - при нажатии клавиши enter
             на реальной клавиатуре именно этот тип IME события вызывается */
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                hideKeyboard()
                true
            } else false
        }
    }

    private fun setupClearButton() {
        binding.clearButton.setOnClickListener {
            binding.searchLine.setText("")

            hideKeyboard()
            binding.searchLine.clearFocus()

            hideSearchResults()
            showHistory()
        }
    }

    private fun startSearchTracks() {
        val searchText = searchSettingsInteractor.getSearchString()

        if (lastSearchText == searchText) {
            showSearchResult()
            return
        }

        hideHistory()
        hideSearchResults()
        showProgressBar()

        tracksInteractor.searchTracks(searchText) { data -> getSearchResults(data) }

        lastSearchText = searchText
    }

    private fun getSearchResults(data: ConsumerData<List<Track>>) {
        runOnUiThread {
            hideProgressBar()
            when (data) {
                is ConsumerData.Data -> {
                    if (data.data.isEmpty()) {
                        showSearchError()
                    } else {
                        (binding.recyclerTrackList.adapter as TrackAdapter).setItems(data.data)
                        showSearchResult()
                    }
                }

                is ConsumerData.Error -> {
                    showServerError()
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.searchProgressBar.show()
    }

    private fun hideProgressBar() {
        binding.searchProgressBar.gone()
    }

    private fun showSearchResult() {
        hideHistory()
        hideSearchResults()

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

    private fun showSearchError() {
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

    private fun showHistory() {
        hideSearchResults()
        (binding.recyclerHistoryList.adapter as TrackAdapter).setItems( searchHistoryInteractor.load() )
        if (searchHistoryInteractor.isHistoryEmpty()) {
            binding.historyGroup.gone()
        } else {
            binding.historyGroup.show()
        }
    }

    private fun hideHistory() {
        binding.historyGroup.gone()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addTrackToHistory(track: Track) {
            searchHistoryInteractor.addTrack(track , object : SearchHistoryInteractor.SearchHistoryConsumer {
                override fun consume(actualHistory: List<Track>) {
                    (binding.recyclerHistoryList.adapter as TrackAdapter).setItems(actualHistory)
                    binding.recyclerHistoryList.adapter?.notifyDataSetChanged()
                }
            })
    }

    private fun addTrackAndStartPlayer(track: Track) {
        addTrackToHistory(track)
        startPlayer(track)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearHistory() {
        searchHistoryInteractor.clear()
        binding.recyclerHistoryList.adapter?.notifyDataSetChanged()
        hideHistory()
    }

    private fun startPlayer(track: Track) {
        (application as PlaylistMakerApplication).actualTrack = track
        if (clickDebouncer.can()) {
            val playerIntent = Intent(this, PlayerActivity::class.java)
            startActivity(playerIntent)
        }
    }
}