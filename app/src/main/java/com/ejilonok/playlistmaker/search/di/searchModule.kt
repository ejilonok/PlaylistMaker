package com.ejilonok.playlistmaker.search.di

import androidx.appcompat.app.AppCompatActivity
import com.ejilonok.playlistmaker.search.data.network.NetworkClient
import com.ejilonok.playlistmaker.search.data.network.RetrofitItunesNetworkClient
import com.ejilonok.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.ejilonok.playlistmaker.search.data.repository.TracksSearchRepositoryImpl
import com.ejilonok.playlistmaker.search.domain.api.interactor.SearchHistoryInteractor
import com.ejilonok.playlistmaker.search.domain.api.interactor.TrackInteractor
import com.ejilonok.playlistmaker.search.domain.api.repository.SearchHistoryRepository
import com.ejilonok.playlistmaker.search.domain.api.repository.TracksSearchRepository
import com.ejilonok.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.ejilonok.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.ejilonok.playlistmaker.search.presentation.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(RetrofitItunesNetworkClient.itunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<NetworkClient> {
        RetrofitItunesNetworkClient(androidContext(), get())
    }

    factory<TracksSearchRepository> {
        TracksSearchRepositoryImpl(get())
    }

    factory<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single(named("search_history_prefs")) {
        androidContext().getSharedPreferences(
            SearchHistoryRepositoryImpl.SHARED_PREFERENCE_HISTORY,
            AppCompatActivity.MODE_PRIVATE)
    }

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(named("search_history_prefs")), get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    viewModel {
        SearchViewModel(get(),get(),get(),
            get { parametersOf(SearchViewModel.CLICK_DEBOUNCE_DELAY) },
            get { parametersOf(SearchViewModel.SEARCH_DEBOUNCE_DELAY) })
    }
}
