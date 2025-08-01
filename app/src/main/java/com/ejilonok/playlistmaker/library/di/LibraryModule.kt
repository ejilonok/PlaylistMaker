package com.ejilonok.playlistmaker.library.di

import androidx.room.Room
import com.ejilonok.playlistmaker.library.data.repository.FavoriteTrackRepositoryImpl
import com.ejilonok.playlistmaker.library.data.db.AppDatabase
import com.ejilonok.playlistmaker.library.data.dto.FavoriteTrackConverter
import com.ejilonok.playlistmaker.library.domain.api.interactor.FavoriteTrackInteractor
import com.ejilonok.playlistmaker.library.domain.api.repository.FavoriteTrackRepository
import com.ejilonok.playlistmaker.library.domain.impl.FavoriteTrackInteractorImpl
import com.ejilonok.playlistmaker.library.presentation.FavoritesViewModel
import com.ejilonok.playlistmaker.library.presentation.LibraryViewModel
import com.ejilonok.playlistmaker.library.ui.FavoritesFragment
import com.ejilonok.playlistmaker.library.ui.PlaylistListFragment
import com.ejilonok.playlistmaker.library.presentation.PlaylistListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {
    viewModel {
        LibraryViewModel()
    }

    viewModel {
        FavoritesViewModel( get() )
    }

    viewModel {
        PlaylistListViewModel()
    }

    factory<PlaylistListFragment> {
        PlaylistListFragment.newInstance()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DATABASE_NAME).build()
    }

    factory<FavoritesFragment> {
        FavoritesFragment.newInstance()
    }

    factory {
        FavoriteTrackConverter()
    }

    factory<FavoriteTrackRepository> {
        FavoriteTrackRepositoryImpl( get(), get() )
    }

    factory<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl( get() )
    }
}
