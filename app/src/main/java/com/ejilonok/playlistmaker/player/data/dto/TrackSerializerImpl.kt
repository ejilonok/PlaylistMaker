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
            Track(
                trackId = -1, // Уникальный идентификатор композиции, primary key.
                trackName = "Empty", // Название композиции
                artistName = "Empty", // Имя исполнителя
                trackTime = "0:00", // Продолжительность трека
                artworkUrl100 = "", // Ссылка на изображение обложки
                collectionName = "", // Название альбома
                releaseDate = "----", // Год релиза трека
                primaryGenreName = "", // Жанр трека
                country  = "", // Страна исполнителя
                previewUrl = "" // Ссылка на аудио-превью композиции
            )
        }
    }
}