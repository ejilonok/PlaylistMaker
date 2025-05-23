package com.ejilonok.playlistmaker.main

import android.app.Application
import com.ejilonok.playlistmaker.main.di.mainModule
import com.ejilonok.playlistmaker.library.di.libraryModule
import com.ejilonok.playlistmaker.player.di.playerModule
import com.ejilonok.playlistmaker.search.di.searchModule
import com.ejilonok.playlistmaker.settings.di.settingsModule
import com.ejilonok.playlistmaker.sharing.di.sharingModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlaylistMakerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlaylistMakerApplication)
            modules(mainModule,
                sharingModule,
                settingsModule,
                libraryModule,
                searchModule,
                playerModule)
        }
    }
}
