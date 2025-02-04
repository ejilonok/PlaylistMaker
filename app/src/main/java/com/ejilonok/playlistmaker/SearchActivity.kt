package com.ejilonok.playlistmaker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ejilonok.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {
    private var searchString : String? = SEARCH_STRING_DEF
    private var binding : ActivitySearchBinding? = null
    private var searchHistory : SearchHistory? = null
    private var trackApiService : TrackApiService? = null
    private var searchTrackAdapter : TrackAdapter? = null
    private val searchRunnable = Runnable {searchTracks()}
    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchText : String = ""
    private var isClickAllowed = true

    companion object {
        private const val SEARCH_STRING = "SEARCH_STRING"
        private const val SEARCH_STRING_DEF = ""

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 600L
    }
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        outState.putString(SEARCH_STRING, searchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        searchString = savedInstanceState.getString(SEARCH_STRING) ?: SEARCH_STRING_DEF
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        binding?.let {
            setContentView(it.root)

            searchString = savedInstanceState?.getString(SEARCH_STRING) ?: SEARCH_STRING_DEF

            val searchLine = it.searchLine
            searchLine.setText(searchString)

            searchTrackAdapter = TrackAdapter(::addTrackAndStartPlayer)
            it.recyclerTrackList.adapter = searchTrackAdapter

            val historyTrackAdapter = TrackAdapter(::startPlayer)
            it.recyclerHistoryList.adapter = historyTrackAdapter
            searchHistory = SearchHistory(getSharedPreferences(SearchHistory.SHARED_PREFERENCE_HISTORY, MODE_PRIVATE), historyTrackAdapter.tracks)

            it.clearButton.setOnClickListener {
                searchLine.setText("")

                hideKeyboard()
                searchLine.clearFocus()

                hideSearchResults()
                showHistory()
            }

            it.updateButton.setOnClickListener {
                lastSearchText = ""
                searchTracks()
            }

            it.clearHistoryButton.setOnClickListener {
                clearHistory()
            }

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
                        it.clearButton.visibility = GONE
                        hideSearchResults()
                        showHistory()
                    } else {
                        it.clearButton.visibility = VISIBLE
                    }

                    searchString = s?.toString() ?: ""
                    searchDebounce()
                }

                override fun afterTextChanged(s: Editable?) {

                }
            }

            searchLine.addTextChangedListener(clearTextWatcher)
            searchLine.setOnFocusChangeListener { _ , hasFocus ->
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


            it.searchBackButton.setOnClickListener { this.finish() }

            trackApiService = (application as PlaylistMakerApplication).getTrackApiService()

            showHistory()
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        if (getPreparedSearchString().isEmpty()) return // если поле ввода оказалось пустым, мы не осуществляем поиск
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun getPreparedSearchString() : String {
        return searchString?.replace(" ","+") ?: ""
    }

    private fun showFatalError(text : String) {
        Toast.makeText(this, text , Toast.LENGTH_LONG).show()
    }

    private fun searchTracks() {
        val searchText = getPreparedSearchString()

        if (lastSearchText == searchText) return

        if (trackApiService == null) {
            showFatalError(getString(R.string.api_exception))
            return
        }

        if (searchTrackAdapter == null) {
            showFatalError(getString(R.string.internal_error))
        }

        binding?.let {
            hideHistory()
            hideSearchResults()
            showProgressBar()
            trackApiService!!.getTracks(searchText)
                .enqueue(object : Callback<TracksResponse> {
                    override fun onResponse(
                        call: Call<TracksResponse>,
                        response: Response<TracksResponse>
                    ) {
                        if (response.isSuccessful) {
                            searchTrackAdapter!!.tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                searchTrackAdapter!!.tracks.addAll(response.body()?.results!!)
                                showSearchResult(it.recyclerTrackList)
                            } else {
                                showSearchResult(it.searchError)
                            }
                        } else {
                            showSearchResult(it.serverError)
                        }
                    }

                    override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                        showSearchResult(it.serverError)
                    }
                })

            lastSearchText = searchText
        }
    }

    private fun showProgressBar() {
        binding?.searchProgressBar?.visibility = VISIBLE
    }

    private fun hideProgressBar() {
        binding?.searchProgressBar?.visibility = GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchResult(recyclerView: RecyclerView) {
        binding?.let {
            hideHistory()
            hideSearchResults()
            recyclerView.visibility = VISIBLE
            recyclerView.scrollToPosition(0)
            it.recyclerTrackList.adapter?.notifyDataSetChanged()
        }
    }

    private fun showSearchResult(textView : TextView) {
        binding?.let {
            hideHistory()
            hideSearchResults()
            textView.visibility = VISIBLE
            if (textView === it.serverError) it.updateButton.visibility = VISIBLE
        }
    }

    private fun hideSearchResults() {
        binding?.let {
            it.searchError.visibility = GONE
            it.serverError.visibility = GONE
            it.recyclerTrackList.visibility = GONE
            it.updateButton.visibility = GONE
        }
        hideProgressBar()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadHistory() {
        searchHistory?.load()
        binding?.recyclerHistoryList?.adapter?.notifyDataSetChanged()
    }

    private fun showHistory() {
        searchHistory?.let {
            loadHistory()
            binding?.historyGroup?.visibility = if (it.tracks.isNotEmpty()) VISIBLE else GONE
        }
    }

    private fun hideHistory() {
        binding?.historyGroup?.visibility = GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addTrackToHistory(track : Track) {
        searchHistory?.let {
            it.addTrack(track)
            binding?.recyclerHistoryList?.adapter?.notifyDataSetChanged()
        }
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
        searchHistory?.let {
            it.clearHistory()
            hideHistory()
            binding?.recyclerHistoryList?.adapter?.notifyDataSetChanged()
        }
    }

    private fun startPlayer(track: Track) {
        (application as PlaylistMakerApplication).actualTrack = track
        if (clickDebounce()) {
            val playerIntent = Intent(this, PlayerActivity::class.java)
            startActivity(playerIntent)
        }
    }
    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}