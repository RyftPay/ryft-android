package com.ryftpay.android.ui.model.googlepay

import org.json.JSONArray
import org.json.JSONObject

internal data class ReadyToPayRequest(
    val existingPaymentMethodRequired: Boolean = true,
    val billingAddressRequired: Boolean
) {
    internal fun toApiV2RequestJson(
        baseApiRequest: BaseApiRequest
    ): JSONObject = JSONObject(
        baseApiRequest.toApiRequestJson().toString()
    ).apply {
        put(EXISTING_PAYMENT_METHOD_REQUIRED_KEY, existingPaymentMethodRequired)
        put(
            PaymentMethod.ALLOWED_PAYMENT_METHODS_KEY,
            JSONArray()
                .put(
                    CardPaymentMethod().toApiV2RequestJson(
                        billingAddressRequired,
                        tokenizationSpecification = null
                    )
                )
        )
    }

    companion object {
        private const val EXISTING_PAYMENT_METHOD_REQUIRED_KEY = "existingPaymentMethodRequired"
    }
}
