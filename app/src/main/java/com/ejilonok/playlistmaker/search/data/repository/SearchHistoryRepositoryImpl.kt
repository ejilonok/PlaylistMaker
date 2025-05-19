package com.ejilonok.playlistmaker.search.data.repository

import android.content.SharedPreferences
import com.ejilonok.playlistmaker.search.domain.api.repository.SearchHistoryRepository
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(
    private val sharedPreferences : SharedPreferences,
    private val gson: Gson) : SearchHistoryRepository {

    override fun load() : List<Track> {
        val jsonHistory = sharedPreferences.getString(TAG, null)
        jsonHistory?.let {
            return gson.fromJson(it, Array<Track>::class.java).toList()
        }
        return listOf()
    }

    override fun save(tracks : List<Track>) {
        val jsonHistory = gson.toJson(tracks)
        sharedPreferences.edit()
            .putString(TAG, jsonHistory)
            .apply()
    }

    companion object {
        const val SHARED_PREFERENCE_HISTORY = "com.ejilonok.playlistmaker.history"
        const val TAG = "HISTORY"
    }
}
