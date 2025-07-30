package com.ejilonok.playlistmaker.search.data.network

import com.ejilonok.playlistmaker.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
    // данный метод не осуществляет сетевого запроса и может выполниться в основном потоке, поэтому его не делаю suspend
    fun isConnected() : Boolean
}