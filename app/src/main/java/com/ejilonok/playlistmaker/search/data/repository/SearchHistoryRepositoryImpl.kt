package com.ejilonok.playlistmaker.search.data.repository

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.search.domain.api.repository.SearchHistoryRepository
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(private val application: Application) : SearchHistoryRepository {
    private val sharedPreferences by lazy { application.getSharedPreferences(
        SHARED_PREFERENCE_HISTORY,
        AppCompatActivity.MODE_PRIVATE
    ) }
    override fun load() : List<Track> {
        val jsonHistory = sharedPreferences.getString(TAG, null)
        jsonHistory?.let {
            return Gson().fromJson(it, Array<Track>::class.java).toList()
        }
        return listOf()
    }

    override fun save(tracks : List<Track>) {
        val jsonHistory = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(TAG, jsonHistory)
            .apply()
    }

    companion object {
        const val SHARED_PREFERENCE_HISTORY = "com.ejilonok.playlistmaker.history"
        const val TAG = "HISTORY"
    }
}