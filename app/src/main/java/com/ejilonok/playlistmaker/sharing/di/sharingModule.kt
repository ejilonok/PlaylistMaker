package com.ejilonok.playlistmaker.sharing.di

import com.ejilonok.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.ejilonok.playlistmaker.sharing.domain.api.interactor.SharingInteractor
import com.ejilonok.playlistmaker.sharing.domain.api.repository.ExternalNavigator
import com.ejilonok.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharingModule = module{
    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext(),get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
}