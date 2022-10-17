package com.ryftpay.android.ui.dropin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RyftDropInDisplayConfiguration(
    val usage: RyftDropInUsage,
    val payButtonTitle: String? = null
) : Parcelable {
    companion object {
        internal val Default: RyftDropInDisplayConfiguration = RyftDropInDisplayConfiguration(
            RyftDropInUsage.Payment
        )
    }
}
