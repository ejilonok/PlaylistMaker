package com.ejilonok.playlistmaker.library.data.db.dao

import androidx.room.*
import com.ejilonok.playlistmaker.library.data.db.FavoriteTrackEntity

@Dao
interface FavoriteTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(track : FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_track_table ORDER BY favoriteId DESC")
    suspend fun getFavoriteTracks() : List<FavoriteTrackEntity>

    @Query("SELECT * FROM favorite_track_table WHERE trackId = :trackId LIMIT 1")
    suspend fun getTrackById(trackId : Int) : FavoriteTrackEntity?

    @Query("DELETE FROM favorite_track_table WHERE trackId = :trackId")
    suspend fun removeFromFavorite(trackId : Int) : Int
}