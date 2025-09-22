package com.ejilonok.playlistmaker.library.domain.api.repository

import android.net.Uri
import java.io.File

interface ImageRepository {
    // сохраняет изображение в локальное хранилище и возвращает имя сохраненного файла
    fun saveImageToStorage(uri : Uri) : String
    fun loadImage(filename : String) : File
    fun deleteImage(filename: String)
}