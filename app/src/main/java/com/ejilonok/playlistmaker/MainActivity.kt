package com.ejilonok.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<LinearLayout>(R.id.search_button)
        searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

//        Репетиция использования тостов двумя способами
//        val searchButtonClickListener : View.OnClickListener = object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                Toast.makeText(this@MainActivity, "Нажали на кнопку поиска!", Toast.LENGTH_SHORT).show()
//            }
//        }
//        searchButton.setOnClickListener(searchButtonClickListener)

        val libraryButton = findViewById<LinearLayout>(R.id.library_button)
        libraryButton.setOnClickListener {
            val libraryIntent = Intent(this, LibraryActivity::class.java)
            startActivity(libraryIntent)
        }

//        Репетиция использования тостов двумя способами
//        libraryButton.setOnClickListener {
//            Toast.makeText(this@MainActivity, "Нажали на кнопку Медиатеки =)", Toast.LENGTH_SHORT).show()
//        }

        val settingsButton = findViewById<LinearLayout>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

//        Репетиция использования тостов двумя способами
//        settingsButton.setOnClickListener{
//            Toast.makeText(this@MainActivity, "Hello world!", Toast.LENGTH_LONG).show()
//        }
    }
}