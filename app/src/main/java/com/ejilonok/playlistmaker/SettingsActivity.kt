package com.ejilonok.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private var themeSwitch : SwitchMaterial? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        themeSwitch = findViewById(R.id.theme_switch)
        themeSwitch?.isChecked = ThemeManager.getSavedThemeState(this)

        themeSwitch?.setOnCheckedChangeListener() { _, isChecked ->
            ThemeManager.setDarkTheme(isChecked)
            ThemeManager.saveThemeState(this, isChecked)
        }

        val shareButton = findViewById<MaterialButton>(R.id.share_app)
        shareButton.setOnClickListener() {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shared_link))
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_invitation)))
        }

        val supportButton = findViewById<MaterialButton>(R.id.support)
        supportButton.setOnClickListener() {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.setData(Uri.parse("mailto:"))
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_title))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_text))
            startActivity(Intent.createChooser(supportIntent, ""))
        }

        val termsButton = findViewById<MaterialButton>(R.id.terms_of_use)
        termsButton.setOnClickListener() {
            val termsIntent = Intent(Intent.ACTION_VIEW)
            termsIntent.data = Uri.parse(getString(R.string.terms_link))
            startActivity(termsIntent)
        }

        val searchButton = findViewById<MaterialButton>(R.id.settings_search_button)
        searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        val libraryButton = findViewById<MaterialButton>(R.id.settings_library_button)
        libraryButton.setOnClickListener {
            val libraryIntent = Intent(this, LibraryActivity::class.java)
            startActivity(libraryIntent)
        }
    }
}