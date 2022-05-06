package com.ryftpay.android.ui.model.googlepay

import org.json.JSONObject

internal abstract class PaymentMethod {

    internal abstract fun toApiV2RequestJson(
        tokenizationSpecification: TokenizationSpecification?
    ): JSONObject

    companion object {
        internal const val ALLOWED_PAYMENT_METHODS_KEY = "allowedPaymentMethods"
    }
}
