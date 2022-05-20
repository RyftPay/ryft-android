package com.ryftpay.android.ui.model.googlepay

import com.ryftpay.android.ui.model.RyftCardType
import org.json.JSONArray
import org.json.JSONObject

internal class CardPaymentMethod : PaymentMethod() {

    override fun toApiV2RequestJson(
        billingAddressRequired: Boolean,
        tokenizationSpecification: TokenizationSpecification?
    ): JSONObject {
        val cardPaymentMethodParameters = JSONObject()
            .put(ALLOWED_AUTH_METHODS_KEY, JSONArray(supportedAuthMethods))
            .put(
                ALLOWED_CARD_NETWORKS_KEY,
                JSONArray(RyftCardType.getGooglePaySupportedTypeNames())
            )
        if (billingAddressRequired) {
            cardPaymentMethodParameters
                .put(BILLING_ADDRESS_REQUIRED_KEY, true)
                .put(
                    BILLING_ADDRESS_PARAMETERS_KEY,
                    JSONObject()
                        .put(BILLING_ADDRESS_FORMAT_KEY, FULL_BILLING_ADDRESS_FORMAT)
                )
        }
        val cardPaymentMethod = JSONObject()
            .put(TYPE_KEY, CARD_TYPE)
            .put(PARAMETERS_KEY, cardPaymentMethodParameters)
        if (tokenizationSpecification != null) {
            cardPaymentMethod.put(
                TokenizationSpecification.KEY,
                tokenizationSpecification.toApiV2RequestJson()
            )
        }
        return cardPaymentMethod
    }

    companion object {
        private const val TYPE_KEY = "type"
        private const val CARD_TYPE = "CARD"
        private const val PARAMETERS_KEY = "parameters"
        private const val ALLOWED_AUTH_METHODS_KEY = "allowedAuthMethods"
        private const val ALLOWED_CARD_NETWORKS_KEY = "allowedCardNetworks"
        private const val BILLING_ADDRESS_REQUIRED_KEY = "billingAddressRequired"
        private const val BILLING_ADDRESS_PARAMETERS_KEY = "billingAddressParameters"
        private const val BILLING_ADDRESS_FORMAT_KEY = "format"
        private const val FULL_BILLING_ADDRESS_FORMAT = "FULL"
        private val supportedAuthMethods = arrayOf("PAN_ONLY", "CRYPTOGRAM_3DS")
    }
}
