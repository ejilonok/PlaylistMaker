package com.ejilonok.playlistmaker.library.data.repository

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.ejilonok.playlistmaker.library.domain.api.repository.ImageRepository
import java.io.File
import java.io.FileOutputStream

class ImageRepositoryImpl(
    private val appContext: Context,
    private val contentResolver: ContentResolver
) : ImageRepository {

    private fun getLocalName() : String {
        return "img_${System.currentTimeMillis()}.jpg"
    }

    // Сохраняет изображение из URI в локальное хранилище
    override fun saveImageToStorage(uri: Uri) : String {
        val filePath = File(getExternalImagePath(), IMAGE_DIRECTORY)

        if (!filePath.exists()){
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val fileName = getLocalName()
        val file = File(filePath, fileName)
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return fileName
    }

    override fun loadImage(filename: String) : File {
        val filePath = File(getExternalImagePath(), IMAGE_DIRECTORY)
        return File(filePath, filename)
    }

    override fun deleteImage(filename: String) {
        val filePath = File(getExternalImagePath(), IMAGE_DIRECTORY)
        val file = File(filePath, filename)
        file.delete()
    }

    private fun getExternalImagePath() : File {
        return appContext.getExternalFilesDirs(Environment.DIRECTORY_PICTURES)[0] ?: appContext.getExternalFilesDirs(null)[0]
    }

    companion object {
        private const val IMAGE_DIRECTORY = "saved_images"
    }
}