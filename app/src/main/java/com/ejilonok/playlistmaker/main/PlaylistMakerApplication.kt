package com.ejilonok.playlistmaker.main

import android.app.Application
import com.ejilonok.playlistmaker.creator.Creator
import com.ejilonok.playlistmaker.search.presenatation.SearchPresenter

class PlaylistMakerApplication : Application() {
    private var searchPresenter : SearchPresenter ?= null

    fun getSearchPresenter() : SearchPresenter {
        if (searchPresenter == null)
            searchPresenter = Creator.provideSearchPresenter(this)
        return searchPresenter!!
    }

    fun clearSearchPresenter() {
        searchPresenter = null
    }
}