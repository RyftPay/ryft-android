package com.ryftpay.android.ui.dropin.threeds

import android.os.Parcelable
import com.ryftpay.android.core.model.payment.RequiredAction
import kotlinx.parcelize.Parcelize

interface RyftRequiredActionComponent {
    fun handle(
        configuration: Configuration,
        requiredAction: RequiredAction
    )

    @Parcelize
    class Configuration private constructor(
        internal val clientSecret: String,
        internal val subAccountId: String?,
        internal val returnUrl: String?
    ) : Parcelable {
        companion object {
            fun subAccountPayment(
                clientSecret: String,
                subAccountId: String,
                returnUrl: String? = null
            ): Configuration = Configuration(
                clientSecret,
                subAccountId,
                returnUrl
            )
            fun standardAccountPayment(
                clientSecret: String,
                returnUrl: String? = null
            ): Configuration = Configuration(
                clientSecret,
                subAccountId = null,
                returnUrl
            )
        }
    }
}
