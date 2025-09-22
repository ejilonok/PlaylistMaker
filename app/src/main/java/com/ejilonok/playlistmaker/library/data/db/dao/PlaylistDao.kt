package com.ejilonok.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ejilonok.playlistmaker.library.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table ORDER BY playlistId DESC")
    fun getPlaylists() : Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table WHERE title = :title LIMIT 1")
    fun getPlaylistByName(title : String) : Flow<PlaylistEntity?>
}