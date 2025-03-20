package com.ejilonok.playlistmaker.search.domain.impl

import com.ejilonok.playlistmaker.search.domain.api.interactor.SearchSettingsInteractor
import com.ejilonok.playlistmaker.search.domain.api.repository.SearchSettingsRepository
import com.ejilonok.playlistmaker.search.domain.models.SearchSettings

class SearchSettingsInteractorImpl(
    private val searchSettingsRepository: SearchSettingsRepository
) : SearchSettingsInteractor {
    private var searchSettings = SearchSettings(SEARCH_STRING_DEF)
    override fun save(str : CharSequence?) : String {
        val text = str?.toString()
        searchSettings.searchString = if (text.isNullOrEmpty()) SEARCH_STRING_DEF else text
        return save()
    }

    override fun save() : String {
        return searchSettingsRepository.save(searchSettings)
    }
    override fun load(from : String?) : String {
        searchSettings = if (from == null) SearchSettings(SEARCH_STRING_DEF) else searchSettingsRepository.load(from)
        return searchSettings.searchString
    }

    override fun getSearchString() : String {
        return searchSettings.searchString
    }

    companion object {
        private const val SEARCH_STRING_DEF = ""
    }
}