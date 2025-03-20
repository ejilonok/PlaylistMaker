package com.ejilonok.playlistmaker.main

import android.app.Application
//TODO здесь должно быть использование интерактора. Или перенести использование интерактора в другой модуль
import com.ejilonok.playlistmaker.search.domain.models.Track

class PlaylistMakerApplication : Application() {
    var actualTrack : Track? = null
}