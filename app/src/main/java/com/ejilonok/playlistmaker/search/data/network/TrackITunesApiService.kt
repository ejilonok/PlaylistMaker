package com.ejilonok.playlistmaker.search.data.network

import com.ejilonok.playlistmaker.search.data.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackITunesApiService {
    @GET("search?entity=song")
    fun searchTracks(
        @Query("term") searchLine : String
    ): Call<TracksSearchResponse>
}