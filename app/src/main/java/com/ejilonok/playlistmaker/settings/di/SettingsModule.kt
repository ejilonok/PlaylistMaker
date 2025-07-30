package com.ejilonok.playlistmaker.settings.di

import android.content.Context
import com.ejilonok.playlistmaker.settings.data.ConfigurationProviderImpl
import com.ejilonok.playlistmaker.settings.data.repository.ThemeManagerImpl
import com.ejilonok.playlistmaker.settings.data.repository.ThemeRepositoryImpl
import com.ejilonok.playlistmaker.settings.domain.api.ConfigurationProvider
import com.ejilonok.playlistmaker.settings.domain.api.interactor.ThemeInteractor
import com.ejilonok.playlistmaker.settings.domain.api.repository.ThemeManager
import com.ejilonok.playlistmaker.settings.domain.api.repository.ThemeRepository
import com.ejilonok.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.ejilonok.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingsModule = module {

    single(named(ThemeRepositoryImpl.SHARED_PREFERENCES_NAME)) {
        androidContext().getSharedPreferences(ThemeRepositoryImpl.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    single<ConfigurationProvider> {
        ConfigurationProviderImpl(androidContext())
    }

    factory<ThemeRepository> {
        ThemeRepositoryImpl(get(), get(named(ThemeRepositoryImpl.SHARED_PREFERENCES_NAME)))
    }

    factory<ThemeManager> {
        ThemeManagerImpl()
    }

    factory<ThemeInteractor> {
        ThemeInteractorImpl(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
}
