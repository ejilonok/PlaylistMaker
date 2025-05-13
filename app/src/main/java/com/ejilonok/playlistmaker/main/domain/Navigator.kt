package com.ejilonok.playlistmaker.main.domain

import com.ejilonok.playlistmaker.search.domain.models.Track

interface Navigator {
    fun gotoSearch()
    fun gotoLibrary()
    fun gotoSettings()
    fun gotoPlayer(track: Track)
}