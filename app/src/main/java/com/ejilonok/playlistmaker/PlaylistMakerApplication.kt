package com.ejilonok.playlistmaker

import android.app.Application
import com.ejilonok.playlistmaker.domain.models.Track

class PlaylistMakerApplication : Application() {
    var actualTrack : Track? = null
}