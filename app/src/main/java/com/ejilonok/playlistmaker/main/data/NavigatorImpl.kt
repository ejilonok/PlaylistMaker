package com.ejilonok.playlistmaker.main.data

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.ejilonok.playlistmaker.main.domain.Navigator
import com.ejilonok.playlistmaker.library.ui.LibraryActivity
import com.ejilonok.playlistmaker.player.domain.api.mapper.TrackSerializer
import com.ejilonok.playlistmaker.player.ui.PlayerActivity
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.ejilonok.playlistmaker.search.ui.SearchActivity
import com.ejilonok.playlistmaker.settings.ui.SettingsActivity

class NavigatorImpl(
    application: Application,
    private val trackConverter : TrackSerializer
) : Navigator {
    private val applicationContext = application.applicationContext
    override fun gotoSearch() {
        startOtherActivity(SearchActivity::class.java)
    }

    override fun gotoLibrary() {
        startOtherActivity(LibraryActivity::class.java)
    }

    override fun gotoSettings() {
        startOtherActivity(SettingsActivity::class.java)
    }

    override fun gotoPlayer(track: Track) {
        val playerIntent = Intent(applicationContext, PlayerActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK  // Добавляем флаг
            putExtra("TRACK_JSON", trackConverter.toString(track))
        }
        applicationContext.startActivity(playerIntent)
    }

    private fun <T : Activity> startOtherActivity(activity : Class<T>) {
        val intent = Intent(applicationContext, activity).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK  // Добавляем флаг
        }
        applicationContext.startActivity(intent)
    }
}