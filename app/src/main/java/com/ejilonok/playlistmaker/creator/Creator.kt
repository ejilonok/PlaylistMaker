package com.ejilonok.playlistmaker.creator

import android.content.Context
import com.ejilonok.playlistmaker.domain.api.interactors.ThemeInteractor
import com.ejilonok.playlistmaker.domain.impl.ThemeInteractorImpl
import com.ejilonok.playlistmaker.domain.api.repository.ThemeRepository
import com.ejilonok.playlistmaker.data.repository.ThemeRepositoryImpl
import com.ejilonok.playlistmaker.data.repository.TracksSearchRepositoryImpl
import com.ejilonok.playlistmaker.data.network.RetrofitItunesNetworkClient
import com.ejilonok.playlistmaker.data.repository.PlayerSettingsRepositoryImpl
import com.ejilonok.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.ejilonok.playlistmaker.data.repository.SearchSettingsRepositoryImpl
import com.ejilonok.playlistmaker.data.repository.ThemeManagerImpl
import com.ejilonok.playlistmaker.domain.api.interactors.PlayerInteractor
import com.ejilonok.playlistmaker.domain.api.interactors.SearchHistoryInteractor
import com.ejilonok.playlistmaker.domain.api.interactors.SearchSettingsInteractor
import com.ejilonok.playlistmaker.domain.api.interactors.TrackInteractor
import com.ejilonok.playlistmaker.domain.api.repository.PlayerSettingsRepository
import com.ejilonok.playlistmaker.domain.api.repository.SearchHistoryRepository
import com.ejilonok.playlistmaker.domain.api.repository.SearchSettingsRepository
import com.ejilonok.playlistmaker.domain.api.repository.ThemeManager
import com.ejilonok.playlistmaker.domain.api.repository.TracksSearchRepository
import com.ejilonok.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.ejilonok.playlistmaker.domain.impl.TrackInteractorImpl
import com.ejilonok.playlistmaker.domain.impl.PlayerInteractorImpl
import com.ejilonok.playlistmaker.domain.impl.SearchSettingsInteractorImpl

object Creator {
    fun provideTracksInteractor() : TrackInteractor {
        return TrackInteractorImpl(getTrackSearchRepository())
    }

    fun provideSearchHistoryInteractor(context : Context) : SearchHistoryInteractor {
        return SearchHistoryInteractorImpl( getSearchHistoryRepository(context) )
    }

    fun providePlayerInteractor() : PlayerInteractor {
        return PlayerInteractorImpl(getPlayerSettingsRepository())
    }

    fun provideThemeInteractor(context: Context) : ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(context), getThemeManager())
    }

    fun provideSearchSettingsInteractor() : SearchSettingsInteractor {
        return SearchSettingsInteractorImpl(getSearchSettingsRepository())
    }

    private fun getTrackSearchRepository() : TracksSearchRepository {
        return  TracksSearchRepositoryImpl(RetrofitItunesNetworkClient)
    }

    private fun getSearchHistoryRepository(context : Context) : SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    private fun getPlayerSettingsRepository() : PlayerSettingsRepository {
        return PlayerSettingsRepositoryImpl()
    }

    private fun getThemeRepository(context: Context) : ThemeRepository {
        return ThemeRepositoryImpl(context)
    }

    private fun getThemeManager() : ThemeManager {
        return ThemeManagerImpl()
    }

    private fun getSearchSettingsRepository() : SearchSettingsRepository {
        return SearchSettingsRepositoryImpl()
    }
}