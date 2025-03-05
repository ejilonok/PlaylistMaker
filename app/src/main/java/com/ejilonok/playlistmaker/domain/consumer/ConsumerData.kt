package com.ejilonok.playlistmaker.domain.consumer

sealed interface ConsumerData<T> {
    class Data<T>(val data : T) : ConsumerData<T>
    class Error<T>(val err_code : Int) : ConsumerData<T>
}