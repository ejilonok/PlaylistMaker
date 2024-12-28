package com.ejilonok.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.databinding.ActivitySettingsBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private var binding : ActivitySettingsBinding? = null
    private var themeSwitch : SwitchMaterial? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        binding?.let {
            setContentView(it.root)

            it.themeSwitch.isChecked = ThemeManager.getSavedThemeState(this)
            it.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
                ThemeManager.setDarkTheme(isChecked)
                ThemeManager.saveThemeState(this, isChecked)
            }

            it.shareButton.setOnClickListener {
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

            it.supportButton.setOnClickListener {
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

            it.termsOfUseButton.setOnClickListener {
                val termsIntent = Intent(Intent.ACTION_VIEW)
                termsIntent.data = Uri.parse(getString(R.string.terms_link))
                startActivity(termsIntent)
            }

            it.settingsBackButton.setOnClickListener {
                finish()
            }
        }
    }
}