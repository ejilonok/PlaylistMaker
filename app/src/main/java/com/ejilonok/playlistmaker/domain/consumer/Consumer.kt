package com.ejilonok.playlistmaker.domain.consumer

fun interface Consumer<T> {
    fun consume(data : ConsumerData<T>)
}