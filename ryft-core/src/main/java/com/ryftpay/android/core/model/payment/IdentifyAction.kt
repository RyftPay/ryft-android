package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.api.payment.IdentifyActionResponse

data class IdentifyAction(
    val scheme: String,
    val messageVersion: String
) {
    companion object {
        internal fun from(response: IdentifyActionResponse): IdentifyAction =
            IdentifyAction(
                scheme = response.scheme,
                messageVersion = response.messageVersion
            )
    }
}
