package com.ejilonok.playlistmaker.library.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoritesViewModel : ViewModel() {
    private var state : MutableLiveData<FavoritesState> = MutableLiveData(FavoritesState.Empty)

    fun getStateLiveData() : LiveData<FavoritesState> = state
}
