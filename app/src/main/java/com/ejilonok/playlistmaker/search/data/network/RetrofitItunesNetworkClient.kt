package com.ejilonok.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.ejilonok.playlistmaker.search.data.dto.Response
import com.ejilonok.playlistmaker.search.data.dto.TracksSearchRequest
import com.ejilonok.playlistmaker.search.domain.models.ResponseCode
import retrofit2.Retrofit

class RetrofitItunesNetworkClient(
    private val context: Context,
    retrofit : Retrofit ) : NetworkClient {
    companion object {
        const val itunesBaseUrl = "https://itunes.apple.com/"
    }

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

    override fun isConnected(): Boolean {
        val connectivityManager = context.applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}