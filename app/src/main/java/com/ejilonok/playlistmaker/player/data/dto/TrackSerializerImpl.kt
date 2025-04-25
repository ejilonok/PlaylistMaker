package com.ejilonok.playlistmaker.player.data.dto

import com.ejilonok.playlistmaker.player.domain.api.mapper.TrackSerializer
import com.ejilonok.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

object TrackSerializerImpl : TrackSerializer {
    private val gson = Gson()
    fun Track.map() : String {
        return gson.toJson(this)
    }

    override fun toString(track: Track) : String = track.map()

    override fun fromString(string: String) : Track {
        return try {
            gson.fromJson(string, Track::class.java)
        } catch (e : Exception) {
            TrackSerializer.EMPTY_TRACK
        }
    }
}