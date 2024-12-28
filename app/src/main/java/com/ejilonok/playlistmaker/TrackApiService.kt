package com.ejilonok.playlistmaker

import retrofit2.http.GET
import retrofit2.http.Query


interface TrackApiService {
    @GET("search?entity=song")
    fun getTracks(
        @Query("term") searchLine : String
    ): retrofit2.Call<TracksResponse>
}