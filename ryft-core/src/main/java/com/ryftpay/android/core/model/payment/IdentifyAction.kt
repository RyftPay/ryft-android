package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.api.payment.IdentifyActionResponse

data class IdentifyAction(
    val scheme: String,
    val paymentMethodId: String,
    val protocolVersion: String,
    val ravelinPublicKey: String
) {
    companion object {
        internal fun from(response: IdentifyActionResponse): IdentifyAction =
            IdentifyAction(
                scheme = response.scheme,
                paymentMethodId = response.paymentMethodId,
                protocolVersion = response.protocolVersion,
                ravelinPublicKey = response.ravelinPublicKey
            )
    }
}
