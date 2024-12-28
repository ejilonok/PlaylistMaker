package com.ejilonok.playlistmaker

import android.app.Application
import android.content.res.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlaylistMakerApplication : Application() {
    private var trackApiService : TrackApiService? = null
    override fun onCreate() {
        super.onCreate()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        trackApiService = retrofit.create(TrackApiService::class.java)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    fun getTrackApiService() : TrackApiService? {
        return trackApiService
    }

}