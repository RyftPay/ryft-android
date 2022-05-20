package com.ryftpay.android.ui.dropin

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class RyftDropInConfiguration(
    val clientSecret: String,
    val subAccountId: String?,
    val googlePayConfiguration: RyftDropInGooglePayConfiguration? = null
) : Parcelable {
    @IgnoredOnParcel
    internal val googlePayEnabled = googlePayConfiguration != null
}
