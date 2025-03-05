package com.ejilonok.playlistmaker.ui.search

import android.os.Bundle
import com.ejilonok.playlistmaker.domain.models.SearchSettings

class SearchSettingsRepositoryImpl {
    private val settings = SearchSettings(SEARCH_STRING_DEF)
    fun save(state : Bundle) {
        state.putString(SEARCH_STRING, settings.searchString)
    }

    fun load(state: Bundle) {
        settings.searchString = state.getString(SEARCH_STRING) ?: SEARCH_STRING_DEF
    }

    fun getSearchString() : String {
        return settings.searchString
    }

    fun saveSearchString(str : CharSequence?) {
        val text = str?.toString()
        settings.searchString = if (text.isNullOrEmpty()) SEARCH_STRING_DEF else text
    }

    companion object {
        private const val SEARCH_STRING = "SEARCH_STRING"
        private const val SEARCH_STRING_DEF = ""
    }
}