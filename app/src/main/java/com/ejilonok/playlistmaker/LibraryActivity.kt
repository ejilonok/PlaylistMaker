package com.ejilonok.playlistmaker

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.ejilonok.playlistmaker.databinding.ActivityLibraryBinding

class LibraryActivity : AppCompatActivity() {
    private var binding: ActivityLibraryBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        binding?.let {
            setContentView(it.root)

            it.libraryBackButton.setOnClickListener {
                finish()
            }
        }
    }
}
