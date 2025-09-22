package com.ejilonok.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "playlist_track_cross_ref",
    primaryKeys = ["playlistId", "trackId"],
    foreignKeys = [ForeignKey(
        entity = PlaylistEntity::class,
        parentColumns = ["playlistId"],
        childColumns = ["playlistId"],
        onDelete = ForeignKey.CASCADE
    )])
// Идентификатор трека не будем делать вторичным ключом, т.к. не будем хранить БД треков на устройстве - это дело БД на сервере
class PlaylistTrackCrossRef (
    val playlistId : Int,
    val trackId : Int
)