package com.ejilonok.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class LibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        val backButton = findViewById<ImageView>(R.id.library_back_button)
        backButton.setOnClickListener {
            finish()
        }
    }
}