package com.ejilonok.playlistmaker.search.data.network

import com.ejilonok.playlistmaker.search.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackITunesApiService {
    @GET("search?entity=song")
    suspend fun searchTracks(
        @Query("term") searchLine : String
    ): TracksSearchResponse
}