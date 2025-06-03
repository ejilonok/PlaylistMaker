package com.ejilonok.playlistmaker.library.di

import com.ejilonok.playlistmaker.library.presentation.FavoritesViewModel
import com.ejilonok.playlistmaker.library.presentation.LibraryViewModel
import com.ejilonok.playlistmaker.library.ui.FavoritesFragment
import com.ejilonok.playlistmaker.library.ui.PlaylistListFragment
import com.ejilonok.playlistmaker.library.presentation.PlaylistListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val libraryModule = module {
    viewModel {
        LibraryViewModel(get { parametersOf(LibraryViewModel.CLICK_DEBOUNCE_DELAY) },
            get())
    }

    viewModel {
        FavoritesViewModel()
    }

    viewModel {
        PlaylistListViewModel()
    }

    factory<PlaylistListFragment> {
        PlaylistListFragment.newInstance()
    }

    factory<FavoritesFragment> {
        FavoritesFragment.newInstance()
    }
}
