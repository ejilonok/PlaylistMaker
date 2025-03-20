package com.ejilonok.playlistmaker.search.data.dto

data class TrackDto (
    val trackId: Int, // Уникальный идентификатор композиции, primary key.
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    val releaseDate: String?, // Год релиза трека
    val primaryGenreName: String?, // Жанр трека
    val country : String?, // Страна исполнителя
    val previewUrl : String // Ссылка на аудио-превью композиции
)
