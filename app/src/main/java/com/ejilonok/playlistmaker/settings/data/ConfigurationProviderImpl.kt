package com.ejilonok.playlistmaker.settings.data

import android.content.Context
import android.content.res.Configuration
import com.ejilonok.playlistmaker.settings.domain.api.ConfigurationProvider

class ConfigurationProviderImpl(
    private val context : Context) : ConfigurationProvider {
    override fun getConfiguration(): Configuration {
        return context.applicationContext.resources.configuration
    }
}
