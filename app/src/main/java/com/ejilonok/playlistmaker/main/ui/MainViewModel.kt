package com.ejilonok.playlistmaker.main.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.databinding.ActivityMainBinding
import com.ejilonok.playlistmaker.main.ui.common.ClickDebouncer

class MainViewModel(
    private val context : Context,
    private val binding : ActivityMainBinding
) : ViewModel() {
    private val clickDebouncer = ClickDebouncer(CLICK_DEBOUNCE)
    private val themeInteractor = Creator.provideThemeInteractor(context)
    private val navigator = Creator.provideNavigator(context)


    fun onCreate() {
        themeInteractor.setSavedTheme()

        binding.searchButton.setOnClickListener {
            if (clickDebouncer.can()) {
                navigator.gotoSearch()
            } }
        binding.libraryButton.setOnClickListener {
            if (clickDebouncer.can()) {
                navigator.gotoLibrary()
            } }
        binding.settingsButton.setOnClickListener {
            if (clickDebouncer.can()) {
                navigator.gotoSettings()
            } }
    }

    fun onDestroy() {
        clickDebouncer.onDestroy()
    }
    companion object {
        private const val CLICK_DEBOUNCE = 500L
        /*fun getViewModelFactory(trackId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                // 1
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val application = checkNotNull(extras[APPLICATION_KEY])

                    return MainViewModel(Creator.getApplicationContext()
                    ) as T
                }
            }*/
    }
}