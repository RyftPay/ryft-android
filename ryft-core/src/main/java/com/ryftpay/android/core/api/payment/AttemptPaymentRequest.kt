package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonProperty
import com.ryftpay.android.core.model.payment.PaymentMethod

data class AttemptPaymentRequest(
    @JsonProperty("clientSecret") val clientSecret: String,
    @JsonProperty("cardDetails") val cardDetails: CardDetailsRequest
) {
    companion object {
        internal fun from(clientSecret: String, paymentMethod: PaymentMethod): AttemptPaymentRequest =
            AttemptPaymentRequest(
                clientSecret = clientSecret,
                cardDetails = CardDetailsRequest.from(paymentMethod.cardDetails)
            )
    }
}
