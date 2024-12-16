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
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private var searchString : String? = SEARCH_STRING_DEF
    private lateinit var searchErrorView : TextView
    private lateinit var serverErrorView : TextView
    private lateinit var recyclerView : RecyclerView
    private lateinit var updateButton : MaterialButton

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
        setContentView(R.layout.activity_search)
        searchString = savedInstanceState?.getString(SEARCH_STRING) ?: SEARCH_STRING_DEF

        val searchLine = findViewById<EditText>(R.id.search_line)
        val clearButton = findViewById<ImageView>(R.id.clear_button)

        searchLine.setText(searchString)

        searchErrorView = findViewById(R.id.search_error)
        serverErrorView = findViewById(R.id.server_error)
        updateButton = findViewById(R.id.update_button)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val trackApiService = retrofit.create(TrackApiService::class.java)
        recyclerView = findViewById<RecyclerView>(R.id.track_list)
        val trackAdapter = TrackAdapter()
        val tracks = trackAdapter.tracks
        recyclerView.adapter = trackAdapter

        clearButton.setOnClickListener {
            searchLine.setText("")

            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            searchLine.clearFocus()

            hideResults()
        }

        updateButton.setOnClickListener {
            searchLine.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        val clearTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearButton.visibility = GONE
                } else
                    clearButton.visibility = VISIBLE

                searchString = s?.toString() ?: ""
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        searchLine.addTextChangedListener(clearTextWatcher)

        val backButton = findViewById<ImageView>(R.id.search_back_button)
        backButton.setOnClickListener { this.finish() }


        searchLine.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackApiService.getTracks( searchLine.text.toString() ).enqueue(object : Callback<TracksResponse> {
                    override fun onResponse(call : Call<TracksResponse>, response : Response<TracksResponse>) {
                        if (response.isSuccessful) {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() ?: false) {
                                tracks.addAll(response.body()?.results!!)
                                showResult(recyclerView)
                                trackAdapter.notifyDataSetChanged()
                                recyclerView.scrollToPosition(0)
                            } else {
                                showResult(searchErrorView)
                            }
                        } else {
                            showResult(serverErrorView)
                        }
                    }

                    override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                        showResult(serverErrorView)
                    }
                })
                true
            }
            false
        }
    }

    fun showResult(recyclerView: RecyclerView) {
        hideResults()
        recyclerView.visibility = VISIBLE
    }

    fun showResult(textView : TextView) {
        hideResults()
        textView.visibility = VISIBLE
        if (textView === serverErrorView) updateButton.visibility = VISIBLE
    }

    fun hideResults() {
        searchErrorView.visibility = GONE
        serverErrorView.visibility = GONE
        recyclerView.visibility = GONE
        updateButton.visibility = GONE
    }
}