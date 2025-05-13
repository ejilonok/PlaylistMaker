package com.ejilonok.playlistmaker.library.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ejilonok.playlistmaker.main.presentation.common.ClickDebouncer
import com.ejilonok.playlistmaker.main.presentation.common.SingleLiveEvent

class LibraryViewModel (
    application: Application
) : AndroidViewModel(application) {
    private val clickDebouncer = ClickDebouncer(CLICK_DEBOUNCE_DELAY)
    private val closeActivityEvent = SingleLiveEvent(Unit)
    val closeActivityEventLiveData : LiveData<Unit> = closeActivityEvent
    fun onBackClicked() {
        if (clickDebouncer.can()) {
            closeActivityEvent.postValue(Unit)
        }
    }

    override fun onCleared() {
        clickDebouncer.clearCalls()
        super.onCleared()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 600L
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    LibraryViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
                }
            }
    }
}