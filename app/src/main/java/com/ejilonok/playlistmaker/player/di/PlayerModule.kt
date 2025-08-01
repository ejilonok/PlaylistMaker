package com.ejilonok.playlistmaker.player.di

import android.media.MediaPlayer
import com.ejilonok.playlistmaker.player.data.dto.TrackSerializerImpl
import com.ejilonok.playlistmaker.player.data.repository.AudioPlayerManagerImpl
import com.ejilonok.playlistmaker.player.domain.AudioPlayerManager
import com.ejilonok.playlistmaker.player.domain.api.interactor.PlayerInteractor
import com.ejilonok.playlistmaker.player.domain.api.mapper.TrackSerializer
import com.ejilonok.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.ejilonok.playlistmaker.player.presentation.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    factory<TrackSerializer> { TrackSerializerImpl(get()) }

    factory {
        MediaPlayer()
    }

    factory<AudioPlayerManager> {
        AudioPlayerManagerImpl(get())
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    viewModel<PlayerViewModel> {
        PlayerViewModel( get(), get(), get(), get() )
    }
}
