package com.ejilonok.playlistmaker

import android.app.Application
import android.content.res.Configuration

class PlaylistMakerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

}