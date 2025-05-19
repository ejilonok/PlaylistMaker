package com.ejilonok.playlistmaker.main.data

import android.content.Context
import androidx.annotation.StringRes
import com.ejilonok.playlistmaker.main.domain.ResourceProvider

class ResourceProviderImpl(
    private val context: Context) : ResourceProvider {
    override fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }
}