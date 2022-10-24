package com.ryftpay.android.ui.dropin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// TODO make this a private constructor in next major version upgrade
@Parcelize
data class RyftDropInConfiguration(
    val clientSecret: String,
    val subAccountId: String?,
    val display: RyftDropInDisplayConfiguration = RyftDropInDisplayConfiguration.Default,
    val googlePayConfiguration: RyftDropInGooglePayConfiguration? = null
) : Parcelable {
    companion object {
        fun subAccountPayment(
            clientSecret: String,
            subAccountId: String,
            display: RyftDropInDisplayConfiguration = RyftDropInDisplayConfiguration.Default,
            googlePayConfiguration: RyftDropInGooglePayConfiguration? = null
        ): RyftDropInConfiguration = RyftDropInConfiguration(
            clientSecret,
            subAccountId,
            display,
            googlePayConfiguration
        )
        fun standardAccountPayment(
            clientSecret: String,
            display: RyftDropInDisplayConfiguration = RyftDropInDisplayConfiguration.Default,
            googlePayConfiguration: RyftDropInGooglePayConfiguration? = null
        ): RyftDropInConfiguration = RyftDropInConfiguration(
            clientSecret,
            subAccountId = null,
            display,
            googlePayConfiguration
        )
    }
}
