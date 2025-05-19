package com.ejilonok.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.main.domain.ResourceProvider
import com.ejilonok.playlistmaker.sharing.domain.api.repository.ExternalNavigator
import com.ejilonok.playlistmaker.sharing.domain.models.EmailData

class ExternalNavigatorImpl (
    private val applicationContext: Context,
    private val resourceProvider: ResourceProvider) : ExternalNavigator {
    override fun shareAppLink() {
        shareLink(getShareAppLink(), getShareAppInvitation())
    }

    override fun openTermLink() {
        openLink(getTermsLink())
    }

    override fun openSupportEmail() {
        openEmail(getSupportEmailData())
    }
    override fun shareLink(link: String, invitation : String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
        }
        applicationContext.startActivity(
            Intent.createChooser(
                shareIntent,
                invitation
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun openLink(link: String) {
        val termsIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(resourceProvider.getString(R.string.terms_link))
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        applicationContext.startActivity(termsIntent)
    }

    override fun openEmail(emailData: EmailData, invitation : String) {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(emailData.email)
            )
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
        }
        applicationContext.startActivity(Intent.createChooser(supportIntent, invitation).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    // В отличие от примера в теории, поместила эти функции здесь,
    // чтобы обращение к ресурсам строк было в data слое.
    // Сообщения и адреса считаю, что лучше поддерживать в ресурсных строках,
    // чтобы поддерживать разные локали
    private fun getShareAppLink(): String {
        return resourceProvider.getString(R.string.shared_link)
    }

    private fun getShareAppInvitation() : String {
        return resourceProvider.getString(R.string.share_invitation)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            resourceProvider.getString(R.string.support_email),
            resourceProvider.getString(R.string.support_title),
            resourceProvider.getString(R.string.support_text))
    }

    private fun getTermsLink(): String {
        return resourceProvider.getString(R.string.terms_link)
    }
}
