package com.ejilonok.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(
    private val sharedPreferences: SharedPreferences,
    val tracks : ArrayList<Track>
) {

    companion object {
        const val SHARED_PREFERENCE_HISTORY = "com.ejilonok.playlistmaker.history"
        const val TAG = "HISTORY"

        const val HISTORY_MAX_SIZE = 10
    }

    fun load() {
        val jsonHistory = sharedPreferences.getString(TAG, null)
        jsonHistory?.let {
            tracks.clear()
            tracks.addAll(Gson().fromJson(it, Array<Track>::class.java))
        }
    }

    fun save() {
        val jsonHistory = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(TAG, jsonHistory)
            .apply()
    }
    fun addTrack(track : Track) {
        if (tracks.contains(track)) {
            tracks.remove(track)
        }
        else if (!canMoreAdd()) {
            tracks.removeAt(HISTORY_MAX_SIZE - 1)
        }

        tracks.add(0, track)
        save()
    }

    fun canMoreAdd() : Boolean {
        return tracks.size < HISTORY_MAX_SIZE
    }

    fun clearHistory() {
        tracks.clear()
        save()
    }

}