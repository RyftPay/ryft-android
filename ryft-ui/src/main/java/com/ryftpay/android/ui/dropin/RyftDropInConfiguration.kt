package com.ryftpay.android.ui.dropin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RyftDropInConfiguration(
    val clientSecret: String,
    val subAccountId: String?,
    val display: RyftDropInDisplayConfiguration = RyftDropInDisplayConfiguration.Default,
    val googlePayConfiguration: RyftDropInGooglePayConfiguration? = null
) : Parcelable
