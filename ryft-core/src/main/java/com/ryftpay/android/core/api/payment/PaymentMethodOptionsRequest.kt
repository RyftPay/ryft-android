package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonProperty
import com.ryftpay.android.core.model.payment.PaymentMethodOptions

data class PaymentMethodOptionsRequest(
    @JsonProperty("store") val store: Boolean
) {
    companion object {
        internal fun from(
            options: PaymentMethodOptions?
        ): PaymentMethodOptionsRequest? = if (options == null) {
            null
        } else PaymentMethodOptionsRequest(
            store = options.store
        )
    }
}
