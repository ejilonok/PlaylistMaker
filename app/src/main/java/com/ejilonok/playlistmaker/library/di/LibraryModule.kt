package com.ejilonok.playlistmaker.library.di

import com.ejilonok.playlistmaker.library.presentation.LibraryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val libraryModule = module {
    viewModel {
        LibraryViewModel(get { parametersOf(LibraryViewModel.CLICK_DEBOUNCE_DELAY) },
            get())
    }
}
