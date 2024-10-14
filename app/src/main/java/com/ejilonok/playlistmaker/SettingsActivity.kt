package com.ejilonok.playlistmaker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private var themeSwitch : SwitchMaterial? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        themeSwitch = findViewById<SwitchMaterial>(R.id.theme_switch)
        themeSwitch!!.isChecked = getSavedThemeState()
        themeSwitch!!.setOnCheckedChangeListener() { _, isChecked ->
            setDarkTheme(isChecked)
            saveThemeState(isChecked)
        }

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
            termsIntent.data = Uri.parse(getString(R.string.terms_link))
            startActivity(termsIntent)
        }
    }

    private fun setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun setDarkTheme(isDark : Boolean) {
        if (isDark)
            setDarkTheme()
        else
            setLightTheme()
    }

    private fun getSavedThemeState() : Boolean {
        val sharedPreferences = getSharedPreferences("ThemeSettings", Context.MODE_PRIVATE)
        val systemTheme = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        return sharedPreferences.getBoolean("isDarkTheme", systemTheme)
    }

    private fun saveThemeState(isDark: Boolean) {
        val sharedPreferences = getSharedPreferences("ThemeSettings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isDarkTheme", isDark)
        editor.apply()
    }
}