package com.ejilonok.playlistmaker.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.domain.api.repository.SearchHistoryRepository
import com.ejilonok.playlistmaker.domain.models.Track
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(private val context: Context) : SearchHistoryRepository {
    private val sharedPreferences by lazy { context.getSharedPreferences(
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