package com.ejilonok.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

class Track (
    val trackId: Int, // Уникальный идентификатор композиции, primary key.
    // ^ По заданию идет прямое указание добавить этот параметр, хотя для дата класса функции equals и hashCode переопределяются автоматически
    // и код поиска эквивалентных песен в истории сработает, хотя и не супер быстро. Сейчас, когда есть ограничение истории в 10 композиций - эта
    // оптимизация не кажется мне необходимой
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Название альбома
    val releaseDate: String, // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country : String // Страна исполнителя
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Track

        return trackId.equals(other.trackId)
    }

    override fun hashCode(): Int {
        return trackId.hashCode()
    }

    override fun toString(): String {
        return "$trackId: $artistName - $trackName (${getTrackTimeString()}) from [$collectionName], RD:[$releaseDate], primaryGenre=${primaryGenreName}, country=$country"
    }

    fun getTrackTimeString() : String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }

}

