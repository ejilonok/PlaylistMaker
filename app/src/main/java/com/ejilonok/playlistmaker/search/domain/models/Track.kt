package com.ejilonok.playlistmaker.search.domain.models

class Track (
    val trackId: Int, // Уникальный идентификатор композиции, primary key.
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Название альбома
    val releaseDate: String, // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country : String, // Страна исполнителя
    val previewUrl : String // Ссылка на аудио-превью композиции
)
{
    /* Я использую не дата класс для оптимизации сравнения треков по их id */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Track

        return trackId == other.trackId
    }

    override fun hashCode(): Int {
        return trackId.hashCode()
    }

    override fun toString(): String {
        return "$trackId: $artistName - $trackName ($trackTime) from [${collectionName}], RD:[$releaseDate], primaryGenre=${primaryGenreName}, country=$country, previewUrl=$previewUrl"
    }
}

