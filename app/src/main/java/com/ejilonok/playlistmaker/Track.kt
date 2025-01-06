package com.ejilonok.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

data class Track (
    val trackId: Int, // Уникальный идентификатор композиции, primary key.
    // ^ По заданию идет прямое указание добавить этот параметр, хотя для дата класса функции equals и hashCode переопределяются автоматически
    // и код поиска эквивалентных песен в истории сработает, хотя и не супер быстро. Сейчас, когда есть ограничение истории в 10 композиций - эта
    // оптимизация не кажется мне необходимой
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String) // Ссылка на изображение обложки
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Track

        if (!trackId.equals(other.trackId)) return false

        return true
    }

    override fun hashCode(): Int {
        return trackId.hashCode()
    }

    override fun toString(): String {
        return "$trackId: $artistName - $trackName (${SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)})"
    }

}

