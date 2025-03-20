package com.ejilonok.playlistmaker.search.data.network

import com.ejilonok.playlistmaker.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}