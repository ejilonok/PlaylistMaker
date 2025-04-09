package com.ejilonok.playlistmaker.creator

import android.content.Context
import com.ejilonok.playlistmaker.databinding.ActivitySearchBinding
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
import com.ejilonok.playlistmaker.search.presenatation.SearchPresenter
import com.ejilonok.playlistmaker.search.presenatation.SearchView
import com.ejilonok.playlistmaker.search.ui.SearchActivity
import com.ejilonok.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.ejilonok.playlistmaker.sharing.domain.api.interactor.SharingInteractor
import com.ejilonok.playlistmaker.sharing.domain.api.repository.ExternalNavigator
import com.ejilonok.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    fun provideSearchPresenter(searchActivity: SearchActivity, searchView: SearchView): SearchPresenter {
        return SearchPresenter(searchActivity, searchView)
    }
    fun provideTracksInteractor(context: Context) : TrackInteractor {
        return TrackInteractorImpl(getTrackSearchRepository(context))
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

    fun provideSharingInteractor(context: Context) : SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context))
    }

    fun provideNavigator(context: Context) : Navigator {
        return NavigatorImpl(context)
    }

    private fun getTrackSearchRepository(context: Context) : TracksSearchRepository {
        return  TracksSearchRepositoryImpl(getRetrofitItunesNetworkClient(context))
    }

    private fun getRetrofitItunesNetworkClient(context: Context) : NetworkClient {
        return RetrofitItunesNetworkClient(context)
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

    private fun getExternalNavigator(context: Context) : ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }
}