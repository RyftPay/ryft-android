package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.api.payment.ChallengeActionResponse

data class ChallengeAction(
    val threeDSServerTransactionId: String,
    val acsTransactionId: String,
    val acsRefNumber: String,
    val acsSignedContent: String
) {
    companion object {
        internal fun from(response: ChallengeActionResponse): ChallengeAction =
            ChallengeAction(
                threeDSServerTransactionId = response.threeDSServerTransactionId,
                acsTransactionId = response.acsTransactionId,
                acsRefNumber = response.acsRefNumber,
                acsSignedContent = response.acsSignedContent
            )
    }
}
