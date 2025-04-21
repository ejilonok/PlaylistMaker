package com.ejilonok.playlistmaker.creator

import android.app.Application
import com.ejilonok.playlistmaker.main.data.NavigatorImpl
import com.ejilonok.playlistmaker.main.domain.Navigator
import com.ejilonok.playlistmaker.settings.domain.api.interactor.ThemeInteractor
import com.ejilonok.playlistmaker.settings.domain.api.repository.ThemeRepository
import com.ejilonok.playlistmaker.settings.domain.api.repository.ThemeManager
import com.ejilonok.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.ejilonok.playlistmaker.settings.data.repository.ThemeRepositoryImpl
import com.ejilonok.playlistmaker.settings.data.repository.ThemeManagerImpl
import com.ejilonok.playlistmaker.search.domain.api.interactor.SearchHistoryInteractor
import com.ejilonok.playlistmaker.search.domain.api.interactor.TrackInteractor
import com.ejilonok.playlistmaker.search.domain.api.repository.SearchHistoryRepository
import com.ejilonok.playlistmaker.search.domain.api.repository.TracksSearchRepository
import com.ejilonok.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.ejilonok.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.ejilonok.playlistmaker.search.data.repository.TracksSearchRepositoryImpl
import com.ejilonok.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.ejilonok.playlistmaker.search.data.network.RetrofitItunesNetworkClient
import com.ejilonok.playlistmaker.player.domain.api.interactor.PlayerInteractor
import com.ejilonok.playlistmaker.player.domain.api.repository.PlayerSettingsRepository
import com.ejilonok.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.ejilonok.playlistmaker.player.data.repository.PlayerSettingsRepositoryImpl
import com.ejilonok.playlistmaker.search.data.network.NetworkClient
import com.ejilonok.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.ejilonok.playlistmaker.sharing.domain.api.interactor.SharingInteractor
import com.ejilonok.playlistmaker.sharing.domain.api.repository.ExternalNavigator
import com.ejilonok.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    fun provideTracksInteractor(application: Application) : TrackInteractor {
        return TrackInteractorImpl(getTrackSearchRepository(application))
    }

    fun provideSearchHistoryInteractor(application: Application) : SearchHistoryInteractor {
        return SearchHistoryInteractorImpl( getSearchHistoryRepository(application) )
    }

    fun providePlayerInteractor() : PlayerInteractor {
        return PlayerInteractorImpl(getPlayerSettingsRepository())
    }

    fun provideThemeInteractor(application: Application) : ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(application), getThemeManager())
    }

    fun provideSharingInteractor(application: Application) : SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(application))
    }

    fun provideNavigator(application: Application) : Navigator {
        return NavigatorImpl(application)
    }

    private fun getTrackSearchRepository(application: Application) : TracksSearchRepository {
        return  TracksSearchRepositoryImpl(getRetrofitItunesNetworkClient(application))
    }

    private fun getRetrofitItunesNetworkClient(application: Application) : NetworkClient {
        return RetrofitItunesNetworkClient(application)
    }

    private fun getSearchHistoryRepository(application: Application) : SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(application)
    }

    private fun getPlayerSettingsRepository() : PlayerSettingsRepository {
        return PlayerSettingsRepositoryImpl()
    }

    private fun getThemeRepository(application: Application) : ThemeRepository {
        return ThemeRepositoryImpl(application)
    }

    private fun getThemeManager() : ThemeManager {
        return ThemeManagerImpl()
    }

    private fun getExternalNavigator(application: Application) : ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }
}