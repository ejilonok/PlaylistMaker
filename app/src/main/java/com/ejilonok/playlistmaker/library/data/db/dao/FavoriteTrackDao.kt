package com.ejilonok.playlistmaker.library.data.db.dao

import androidx.room.*
import com.ejilonok.playlistmaker.library.data.db.FavoriteTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(track : FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_track_table ORDER BY favoriteId DESC")
    fun getFavoriteTracks() : Flow<List<FavoriteTrackEntity>>

    @Query("SELECT * FROM favorite_track_table WHERE trackId = :trackId LIMIT 1")
    fun getTrackById(trackId : Int) : Flow<FavoriteTrackEntity?>

    @Query("DELETE FROM favorite_track_table WHERE trackId = :trackId")
    suspend fun removeFromFavorite(trackId : Int) : Int
}