package com.ejilonok.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.main.ui.common.ClickDebouncer
import com.ejilonok.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingsBinding
    private val clickDebouncer = ClickDebouncer(500L)
    private val themeInteractor = Creator.provideThemeInteractor(this)
    private val sharingInteractor = Creator.provideSharingInteractor(this)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.themeSwitch.isChecked = themeInteractor.isSavedThemeDark()
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            themeInteractor.setDarkTheme(isChecked)
        }

        binding.shareButton.setOnClickListener { share() }
        binding.supportButton.setOnClickListener { support() }
        binding.termsOfUseButton.setOnClickListener { termsOfUse() }
        binding.settingsBackButton.setOnClickListener { finish() }
    }

    private fun share() {
        if (clickDebouncer.can()) {
            sharingInteractor.shareApp()
        }
    }

    private fun support() {
        if (clickDebouncer.can()) {
            sharingInteractor.openSupport()
        }
    }

    private fun termsOfUse() {
        if (clickDebouncer.can()) {
            sharingInteractor.openTerms()
        }
    }
}