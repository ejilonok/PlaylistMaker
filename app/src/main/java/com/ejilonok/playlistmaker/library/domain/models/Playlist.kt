package com.ejilonok.playlistmaker.library.domain.models

import com.ejilonok.playlistmaker.search.domain.models.Track

data class Playlist (
    val playlistId : Int = 0, // Уникальный идентификатор плейлиста
    val title : String, // Название плейлиста
    val description : String = "", // Описание плейлиста
    val coverFilename : String = "", // Адрес обложки в локальном хранилище
    var tracks : List<Track> = listOf() // Добавленные треки
)