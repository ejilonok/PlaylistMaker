package com.ejilonok.playlistmaker

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchLine = findViewById<EditText>(R.id.search_line)
        val clearButton = findViewById<ImageView>(R.id.clear_button)

        clearButton.setOnClickListener() {
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
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        searchLine.addTextChangedListener(clearTextWatcher)

    }
}