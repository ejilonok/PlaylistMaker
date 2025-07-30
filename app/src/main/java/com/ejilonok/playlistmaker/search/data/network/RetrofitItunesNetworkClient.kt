package com.ejilonok.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.ejilonok.playlistmaker.search.data.dto.Response
import com.ejilonok.playlistmaker.search.data.dto.TracksSearchRequest
import com.ejilonok.playlistmaker.search.domain.models.ResponseCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class RetrofitItunesNetworkClient(
    private val context: Context,
    retrofit : Retrofit ) : NetworkClient {

    private val itunesService = retrofit.create(TrackITunesApiService::class.java)

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is TracksSearchRequest)
            return Response().apply { resultCode = ResponseCode.NO_ANSWER.code }

        return withContext(Dispatchers.IO) {
            try {
                val resp = itunesService.searchTracks(dto.expression)
                resp.apply { resultCode = ResponseCode.ALL_OK.code }
            } catch (e: Exception) {
                Response().apply { resultCode = ResponseCode.NO_ANSWER.code }
            }
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

    companion object {
        const val itunesBaseUrl = "https://itunes.apple.com/"
    }
}
