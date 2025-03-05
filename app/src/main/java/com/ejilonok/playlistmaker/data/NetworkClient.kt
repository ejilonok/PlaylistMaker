package com.ejilonok.playlistmaker.data

import com.ejilonok.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}