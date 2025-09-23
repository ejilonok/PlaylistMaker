package com.ejilonok.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ejilonok.playlistmaker.library.data.db.dao.FavoriteTrackDao
import com.ejilonok.playlistmaker.library.data.db.dao.PlaylistDao
import com.ejilonok.playlistmaker.library.data.db.entity.FavoriteTrackEntity
import com.ejilonok.playlistmaker.library.data.db.entity.PlaylistEntity

@Database(version = 1, entities = [FavoriteTrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTrackDao() : FavoriteTrackDao
    abstract fun playlistDao() : PlaylistDao

    companion object {
        const val DATABASE_NAME = "database.db"
    }
}