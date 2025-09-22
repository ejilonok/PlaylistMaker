package com.ejilonok.playlistmaker.library.domain.api.interactor

import android.net.Uri

interface CoverInteractor {
    // Метод сохраняет обложку в файлы приложения и возвращает имя созданенного файла
    fun saveCover(uri : Uri) : String
    // Принимает имя файла в хранилище приложения
    fun getCover(filename : String) : Uri
    // Принимает имя файла в хранилище приложения
    fun deleteCover(filename : String)
}