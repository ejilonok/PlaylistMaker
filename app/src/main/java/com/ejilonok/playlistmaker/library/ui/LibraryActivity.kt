package com.ejilonok.playlistmaker.library.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.databinding.ActivityLibraryBinding
import com.ejilonok.playlistmaker.library.presentation.LibraryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLibraryBinding
    private val libraryModel : LibraryViewModel by viewModel()
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()

        binding.viewPager.adapter = FragmentAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(TAB_NAMES[position])
        }
        tabMediator.attach()
    }

    private fun setupUi() {
        libraryModel.closeActivityEventLiveData.observe(this) {
            finish()
        }

        binding.libraryBackButton.setOnClickListener {
            libraryModel.onBackClicked()
        }
    }

    override fun onDestroy() {
        tabMediator.detach()
        super.onDestroy()
    }

    companion object {
        val TAB_NAMES = arrayOf(R.string.favorite_tracks, R.string.playlists)
    }
}
