package com.ejilonok.playlistmaker.player.domain.api.mapper

import com.ejilonok.playlistmaker.search.domain.models.Track

interface TrackSerializer {
    fun toString(track: Track) : String
    fun fromString(string: String) : Track

    companion object {
        val EMPTY_TRACK = Track(
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