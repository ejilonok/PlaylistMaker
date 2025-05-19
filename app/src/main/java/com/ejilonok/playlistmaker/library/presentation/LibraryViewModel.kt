package com.ejilonok.playlistmaker.library.presentation
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ejilonok.playlistmaker.main.presentation.common.ClickDebouncer
import com.ejilonok.playlistmaker.main.presentation.common.SingleLiveEvent

class LibraryViewModel (
    private val clickDebouncer : ClickDebouncer,
    private val closeActivityEvent : SingleLiveEvent<Unit>
) : ViewModel() {
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
        const val CLICK_DEBOUNCE_DELAY = 600L
    }
}
