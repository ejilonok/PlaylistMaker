package com.ejilonok.playlistmaker.main.domain.consumer

fun interface Consumer<T> {
    fun consume(data : ConsumerData<T>)
}