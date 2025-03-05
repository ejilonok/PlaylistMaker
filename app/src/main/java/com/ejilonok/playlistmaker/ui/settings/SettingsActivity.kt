package com.ejilonok.playlistmaker.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.databinding.ActivitySettingsBinding
import com.ejilonok.playlistmaker.ui.common.ClickDebouncer

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingsBinding
    private val clickDebouncer = ClickDebouncer(500L)
    private val themeInteractor = Creator.provideThemeInteractor(this)

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
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shared_link))
            startActivity(
                Intent.createChooser(
                    shareIntent,
                    getString(R.string.share_invitation)
                )
            )
        }
    }

    private fun support() {
        if (clickDebouncer.can()) {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.setData(Uri.parse("mailto:"))
            supportIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.support_email))
            )
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_title))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_text))
            startActivity(Intent.createChooser(supportIntent, ""))
        }
    }

    private fun termsOfUse() {
        if (clickDebouncer.can()) {
            val termsIntent = Intent(Intent.ACTION_VIEW)
            termsIntent.data = Uri.parse(getString(R.string.terms_link))
            startActivity(termsIntent)
        }
    }
}