package com.ejilonok.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ejilonok.playlistmaker.databinding.FragmentSettingsBinding
import com.ejilonok.playlistmaker.main.ui.common.BindingFragment
import com.ejilonok.playlistmaker.settings.presentation.SettingsActions
import com.ejilonok.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {
    private val settingsModel : SettingsViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsModel.actualThemeIsDarkLiveData.observe(viewLifecycleOwner) {
            binding.themeSwitch.isChecked = it ?: false
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsModel.setThemeDark(isChecked)
        }

        binding.shareButton.setOnClickListener { settingsModel.actionDebounce(SettingsActions.ShareAppClicked) }
        binding.supportButton.setOnClickListener { settingsModel.actionDebounce(SettingsActions.SupportClicked) }
        binding.termsOfUseButton.setOnClickListener { settingsModel.actionDebounce(SettingsActions.TermsOfUseClicked) }
    }
}
