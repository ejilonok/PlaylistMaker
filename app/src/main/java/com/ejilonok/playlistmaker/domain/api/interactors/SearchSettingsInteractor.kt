package com.ejilonok.playlistmaker.domain.api.interactors

interface SearchSettingsInteractor {
    fun save(str : CharSequence?) : String
    fun save() : String
    fun load(from : String?) : String
    fun getSearchString() : String
}