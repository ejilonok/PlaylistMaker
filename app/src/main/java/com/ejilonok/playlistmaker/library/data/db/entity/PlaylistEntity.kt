package com.ejilonok.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
class PlaylistEntity (
    @PrimaryKey(autoGenerate = true)
    val playlistId : Int = 0, // Уникальный идентификатор плейлиста
    val title : String, // Название плейлиста
    val description : String = "", // Описание плейлиста
    val coverFilename : String = "", // Адрес обложки в локальном хранилище
)