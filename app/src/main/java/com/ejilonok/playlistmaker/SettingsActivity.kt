package com.ejilonok.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val shareButton = findViewById<MaterialButton>(R.id.share_app)
        shareButton.setOnClickListener() {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setDataAndType(Uri.parse("mailto:"), "text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shared_link))
            startActivity(shareIntent)
        }

        val supportButton = findViewById<MaterialButton>(R.id.support)
        supportButton.setOnClickListener() {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.support_email))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_title))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_text))
            startActivity(supportIntent)
        }

        val termsButton = findViewById<MaterialButton>(R.id.terms_of_use)
        termsButton.setOnClickListener() {
            val termsIntent = Intent(Intent.ACTION_VIEW)
            termsIntent.data = Uri.parse("https:////yandex.ru/legal/practicum_offer/")
            startActivity(termsIntent)
        }
    }
}