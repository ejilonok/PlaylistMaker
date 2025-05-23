package com.ejilonok.playlistmaker.main.domain

import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes resId: Int): String
}
