package com.ejilonok.playlistmaker.search.domain.api.interactor

interface SearchSettingsInteractor {
    fun save(str : CharSequence?) : String
    fun save() : String
    fun load(from : String?) : String
    fun getSearchString() : String
}