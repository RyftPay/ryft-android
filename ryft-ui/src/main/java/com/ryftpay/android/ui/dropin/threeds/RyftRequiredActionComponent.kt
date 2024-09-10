package com.ryftpay.android.ui.dropin.threeds

import android.os.Parcelable
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import com.ryftpay.android.core.model.payment.RequiredAction
import com.ryftpay.android.ui.util.NullableRyftPublicApiKeyParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

interface RyftRequiredActionComponent {
    fun handle(
        configuration: Configuration,
        requiredAction: RequiredAction
    )

    @Parcelize
    class Configuration private constructor(
        internal val clientSecret: String,
        internal val subAccountId: String?,
        internal val returnUrl: String?,
        // TODO make publicApiKey non-null in next major version upgrade
        internal val publicApiKey: @WriteWith<NullableRyftPublicApiKeyParceler> RyftPublicApiKey? = null
    ) : Parcelable {
        companion object {
            fun subAccountPayment(
                clientSecret: String,
                subAccountId: String,
                returnUrl: String? = null,
                publicApiKey: RyftPublicApiKey? = null,
            ): Configuration = Configuration(
                clientSecret,
                subAccountId,
                returnUrl,
                publicApiKey
            )
            fun standardAccountPayment(
                clientSecret: String,
                returnUrl: String? = null,
                publicApiKey: RyftPublicApiKey? = null,
            ): Configuration = Configuration(
                clientSecret,
                subAccountId = null,
                returnUrl,
                publicApiKey
            )
        }
    }
}
