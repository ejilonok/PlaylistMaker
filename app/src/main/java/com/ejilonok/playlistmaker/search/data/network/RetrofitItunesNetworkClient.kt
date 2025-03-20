package com.ejilonok.playlistmaker.search.data.network

import com.ejilonok.playlistmaker.search.data.dto.Response
import com.ejilonok.playlistmaker.search.data.dto.TracksSearchRequest
import com.ejilonok.playlistmaker.search.domain.models.ResponseCode
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitItunesNetworkClient : NetworkClient {

    private val itunesBaseUrl = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(TrackITunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            try {
                val resp = itunesService.searchTracks(dto.expression).execute()
                val body = resp.body() ?: Response()

                body.apply { resultCode = resp.code() }
            } catch (e: Exception) {
                Response().apply { resultCode = ResponseCode.NO_ANSWER.code }
            }
        } else {
            Response().apply { resultCode = ResponseCode.NO_ANSWER.code }
        }
    }
}