package com.ejilonok.playlistmaker.main.di

import android.os.Handler
import android.os.Looper
import com.ejilonok.playlistmaker.main.data.ResourceProviderImpl
import com.ejilonok.playlistmaker.main.domain.ResourceProvider
import com.ejilonok.playlistmaker.main.presentation.MainViewModel
import com.ejilonok.playlistmaker.main.presentation.common.ClickDebouncer
import com.ejilonok.playlistmaker.main.presentation.common.TextInputDebouncer
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    factory { (clickDebounceDelay : Long) ->
        ClickDebouncer(clickDebounceDelay)
    }

    factory<TextInputDebouncer> { (delay : Long) ->
        TextInputDebouncer(get(), delay)
    }

    single<ResourceProvider> {
        ResourceProviderImpl(androidContext())
    }

    factory {
        Gson()
    }

    single<Handler> {
        Handler(Looper.getMainLooper())
    }

    viewModel {
        MainViewModel(get())
    }
}
