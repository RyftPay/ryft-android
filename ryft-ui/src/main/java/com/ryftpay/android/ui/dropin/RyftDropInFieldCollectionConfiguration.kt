package com.ryftpay.android.ui.dropin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RyftDropInFieldCollectionConfiguration(
    val nameOnCard: Boolean
) : Parcelable {
    companion object {
        internal val Default: RyftDropInFieldCollectionConfiguration = RyftDropInFieldCollectionConfiguration(
            nameOnCard = false
        )
    }
}
