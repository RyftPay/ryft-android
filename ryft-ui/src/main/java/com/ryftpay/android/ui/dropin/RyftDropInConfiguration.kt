package com.ryftpay.android.ui.dropin

import android.os.Parcelable
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import com.ryftpay.android.ui.util.NullableRyftPublicApiKeyParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

// TODO make this a private constructor in next major version upgrade
// TODO make publicApiKey non-null in next major version upgrade
@Parcelize
data class RyftDropInConfiguration(
    val clientSecret: String,
    val subAccountId: String?,
    val display: RyftDropInDisplayConfiguration = RyftDropInDisplayConfiguration.Default,
    val fieldCollection: RyftDropInFieldCollectionConfiguration = RyftDropInFieldCollectionConfiguration.Default,
    val googlePayConfiguration: RyftDropInGooglePayConfiguration? = null,
    val publicApiKey: @WriteWith<NullableRyftPublicApiKeyParceler> RyftPublicApiKey? = null
) : Parcelable {
    companion object {
        fun subAccountPayment(
            clientSecret: String,
            subAccountId: String,
            display: RyftDropInDisplayConfiguration = RyftDropInDisplayConfiguration.Default,
            fieldCollection: RyftDropInFieldCollectionConfiguration = RyftDropInFieldCollectionConfiguration.Default,
            googlePayConfiguration: RyftDropInGooglePayConfiguration? = null,
            publicApiKey: RyftPublicApiKey? = null
        ): RyftDropInConfiguration = RyftDropInConfiguration(
            clientSecret,
            subAccountId,
            display,
            fieldCollection,
            googlePayConfiguration,
            publicApiKey
        )
        fun standardAccountPayment(
            clientSecret: String,
            display: RyftDropInDisplayConfiguration = RyftDropInDisplayConfiguration.Default,
            fieldCollection: RyftDropInFieldCollectionConfiguration = RyftDropInFieldCollectionConfiguration.Default,
            googlePayConfiguration: RyftDropInGooglePayConfiguration? = null,
            publicApiKey: RyftPublicApiKey? = null
        ): RyftDropInConfiguration = RyftDropInConfiguration(
            clientSecret,
            subAccountId = null,
            display,
            fieldCollection,
            googlePayConfiguration,
            publicApiKey
        )
    }
}
