package com.ejilonok.playlistmaker.library.domain.impl

import android.net.Uri
import androidx.core.net.toUri
import com.ejilonok.playlistmaker.library.domain.api.interactor.CoverInteractor
import com.ejilonok.playlistmaker.library.domain.api.repository.ImageRepository

class CoverInteractorImpl(
    private val imageRepository : ImageRepository
) : CoverInteractor{
    override fun saveCover(uri: Uri): String {
        return imageRepository.saveImageToStorage(uri)
    }

    override fun getCover(filename: String) : Uri {
        return imageRepository.loadImage(filename).toUri()
    }

    override fun deleteCover(filename: String) {
        TODO("Not yet implemented")
    }
}