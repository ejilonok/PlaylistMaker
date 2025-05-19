package com.ejilonok.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.databinding.ActivitySettingsBinding
import com.ejilonok.playlistmaker.settings.presentation.SettingsActions
import com.ejilonok.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingsBinding
    private val settingsModel : SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        settingsModel.actualThemeIsDarkLiveData.observe(this) {
            binding.themeSwitch.isChecked = it ?: false
        }
        settingsModel.closeActivityEventLiveData.observe(this) {
            finish()
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsModel.setThemeDark(isChecked)
        }

        binding.shareButton.setOnClickListener { settingsModel.onAction(SettingsActions.ShareAppClicked) }
        binding.supportButton.setOnClickListener { settingsModel.onAction(SettingsActions.SupportClicked) }
        binding.termsOfUseButton.setOnClickListener { settingsModel.onAction(SettingsActions.TermsOfUseClicked) }
        binding.settingsBackButton.setOnClickListener { settingsModel.onAction(SettingsActions.BackButtonClicked) }
    }
}