package com.ejilonok.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ejilonok.playlistmaker.library.data.db.dao.FavoriteTrackDao

@Database(version = 1, entities = [FavoriteTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTrackDao() : FavoriteTrackDao

    companion object {
        const val DATABASE_NAME = "database.db"
    }
}