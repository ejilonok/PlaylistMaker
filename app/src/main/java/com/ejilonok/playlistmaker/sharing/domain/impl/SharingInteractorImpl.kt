package com.ejilonok.playlistmaker.sharing.domain.impl

import com.ejilonok.playlistmaker.sharing.domain.api.interactor.SharingInteractor
import com.ejilonok.playlistmaker.sharing.domain.api.repository.ExternalNavigator

class SharingInteractorImpl(
    private val externalNavigator : ExternalNavigator
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareAppLink()
    }

    override fun openTerms() {
        externalNavigator.openTermLink()
    }

    override fun openSupport() {
        externalNavigator.openSupportEmail()
    }
}