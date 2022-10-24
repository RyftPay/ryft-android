package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.api.payment.IdentifyActionResponse

data class IdentifyAction(
    val sessionId: String,
    val sessionSecret: String,
    val scheme: String,
    val paymentMethodId: String
) {
    companion object {
        internal fun from(response: IdentifyActionResponse): IdentifyAction =
            IdentifyAction(
                sessionId = response.sessionId,
                sessionSecret = response.sessionSecret,
                scheme = response.scheme,
                paymentMethodId = response.paymentMethodId
            )
    }
}
