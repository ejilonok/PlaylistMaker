package com.ejilonok.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backward = findViewById<ImageView>(R.id.backward)
        backward.setOnClickListener {
            val mainIntent = Intent(this@SettingsActivity, MainActivity::class.java)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Чтобы не наращивать стек открытых активити, уберем верхнюю со стека во время запуска
            startActivity(mainIntent)
        }
    }
}