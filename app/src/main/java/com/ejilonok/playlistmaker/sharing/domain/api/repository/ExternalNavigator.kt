package com.ejilonok.playlistmaker.sharing.domain.api.repository

import com.ejilonok.playlistmaker.sharing.domain.models.EmailData

interface ExternalNavigator {
    fun shareAppLink()

    fun openTermLink()

    fun openSupportEmail()
    fun shareLink(link : String, invitation : String = "")

    fun openLink(link : String)

    fun openEmail(emailData : EmailData, invitation : String = "")

}