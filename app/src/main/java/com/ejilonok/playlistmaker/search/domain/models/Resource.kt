package com.ejilonok.playlistmaker.search.domain.models

sealed interface Resource<T> {
    class Success<T>(val data: T) : Resource<T>
    class Error<T>(val errCode : Int) : Resource<T>

    enum class ResourceErrorCodes(val code : Int) {
        NETWORK_ERROR(-1)
    }
}