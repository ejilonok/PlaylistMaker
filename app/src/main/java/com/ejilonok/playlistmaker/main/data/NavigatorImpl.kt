package com.ejilonok.playlistmaker.main.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.ejilonok.playlistmaker.main.domain.Navigator
import com.ejilonok.playlistmaker.library.ui.LibraryActivity
import com.ejilonok.playlistmaker.search.ui.SearchActivity
import com.ejilonok.playlistmaker.settings.ui.SettingsActivity

class NavigatorImpl(
    private val context: Context
) : Navigator {
    override fun gotoSearch() {
        startOtherActivity(SearchActivity::class.java)
    }

    override fun gotoLibrary() {
        startOtherActivity(LibraryActivity::class.java)
    }

    override fun gotoSettings() {
        startOtherActivity(SettingsActivity::class.java)
    }

    private fun <T : Activity> startOtherActivity(activity : Class<T>) {
        val intent = Intent(context, activity)
        context.startActivity(intent)
    }
}