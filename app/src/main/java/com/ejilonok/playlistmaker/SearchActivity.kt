package com.ejilonok.playlistmaker

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ejilonok.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private var searchString : String? = SEARCH_STRING_DEF
    private var binding : ActivitySearchBinding? = null

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        const val SEARCH_STRING_DEF = ""
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

            val trackAdapter = TrackAdapter()
            val tracks = trackAdapter.tracks
            it.recyclerTrackList.adapter = trackAdapter

            it.clearButton.setOnClickListener {
                searchLine.setText("")

                // Скрытие клавиатуры
                val inputMethodManager =
                    getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                searchLine.clearFocus()

                hideResults()
            }

            it.updateButton.setOnClickListener {
                searchLine.onEditorAction(EditorInfo.IME_ACTION_DONE)
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
                    } else
                        it.clearButton.visibility = VISIBLE

                    searchString = s?.toString() ?: ""
                }

                override fun afterTextChanged(s: Editable?) {

                }
            }

            searchLine.addTextChangedListener(clearTextWatcher)

            it.searchBackButton.setOnClickListener { this.finish() }

            val trackApiService = (application as PlaylistMakerApplication).getTrackApiService()

            if (trackApiService == null) {
                Toast.makeText(this, "Ошибка открытия сервиса. Попробуйте перезапустить приложение", Toast.LENGTH_LONG)
            } else {
                searchLine.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        trackApiService!!.getTracks(searchLine.text.toString())
                            .enqueue(object : Callback<TracksResponse> {
                                override fun onResponse(
                                    call: Call<TracksResponse>,
                                    response: Response<TracksResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        tracks.clear()
                                        if (response.body()?.results?.isNotEmpty() ?: false) {
                                            tracks.addAll(response.body()?.results!!)
                                            showResult(it.recyclerTrackList)
                                            trackAdapter.notifyDataSetChanged()
                                            it.recyclerTrackList.scrollToPosition(0)
                                        } else {
                                            showResult(it.searchError)
                                        }
                                    } else {
                                        showResult(it.serverError)
                                    }
                                }

                                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                                    showResult(it.serverError)
                                }
                            })
                        true
                    }
                    false
                }
            }
        }
    }

    fun showResult(recyclerView: RecyclerView) {
        binding?.let {
            hideResults()
            recyclerView.visibility = VISIBLE
        }
    }

    fun showResult(textView : TextView) {
        binding?.let {
            hideResults()
            textView.visibility = VISIBLE
            if (textView === it.serverError) it.updateButton.visibility = VISIBLE
        }
    }

    fun hideResults() {
        binding?.let {
            it.searchError.visibility = GONE
            it.serverError.visibility = GONE
            it.recyclerTrackList.visibility = GONE
            it.updateButton.visibility = GONE
        }
    }
}