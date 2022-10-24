package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonProperty

data class PaymentMethodRequest(
    @JsonProperty("id") val id: String
) {
    companion object {
        internal fun from(paymentMethodId: String): PaymentMethodRequest =
            PaymentMethodRequest(paymentMethodId)
    }
}
