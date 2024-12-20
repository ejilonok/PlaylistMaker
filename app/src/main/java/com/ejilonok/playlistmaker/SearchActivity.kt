package com.ejilonok.playlistmaker

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private var searchString : String? = SEARCH_STRING_DEF

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
        clearButton.setOnClickListener {
            searchLine.setText("")

            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            searchLine.clearFocus()
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

        val recyclerView = findViewById<RecyclerView>(R.id.track_list)
        val trackAdapter = TrackAdapter(TrackMock.createMock())
        recyclerView.adapter = trackAdapter
    }
}