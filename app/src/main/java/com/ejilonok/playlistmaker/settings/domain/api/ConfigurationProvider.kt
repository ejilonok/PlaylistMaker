package com.ejilonok.playlistmaker.settings.domain.api

import android.content.res.Configuration

interface ConfigurationProvider {
    fun getConfiguration() : Configuration
}