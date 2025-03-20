package com.ejilonok.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.sharing.domain.api.repository.ExternalNavigator
import com.ejilonok.playlistmaker.sharing.domain.models.EmailData

class ExternalNavigatorImpl (private val context: Context) : ExternalNavigator {
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
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                invitation
            )
        )
    }

    override fun openLink(link: String) {
        val termsIntent = Intent(Intent.ACTION_VIEW)
        termsIntent.data = Uri.parse(context.getString(R.string.terms_link))
        context.startActivity(termsIntent)
    }

    override fun openEmail(emailData: EmailData, invitation : String) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.setData(Uri.parse("mailto:"))
        supportIntent.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(emailData.email)
        )
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
        supportIntent.putExtra(Intent.EXTRA_TEXT, emailData.text)
        context.startActivity(Intent.createChooser(supportIntent, invitation))
    }

    // В отличие от примера в теории, поместила эти функции здесь,
    // чтобы обращение к ресурсам строк было в data слое.
    // Сообщения и адреса считаю, что лучше поддерживать в ресурсных строках,
    // чтобы поддерживать разные локали
    private fun getShareAppLink(): String {
        return context.getString(R.string.shared_link)
    }

    private fun getShareAppInvitation() : String {
        return context.getString(R.string.share_invitation)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            context.getString(R.string.support_email),
            context.getString(R.string.support_title),
            context.getString(R.string.support_text))
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.terms_link)
    }
}