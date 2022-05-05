package com.ryftpay.android.ui.model.googlepay

import com.ryftpay.android.core.model.api.RyftPublicApiKey
import org.json.JSONObject

data class TokenizationSpecification(
    val gateway: String,
    val gatewayMerchantId: String
) {
    fun toApiV2RequestJson(): JSONObject = JSONObject()
        .put(TYPE_KEY, PAYMENT_GATEWAY_TYPE)
        .put(
            PARAMETERS_KEY,
            JSONObject()
                .put(GATEWAY_KEY, gateway)
                .put(GATEWAY_MERCHANT_ID_KEY, gatewayMerchantId)
        )

    companion object {
        const val KEY = "tokenizationSpecification"
        private const val TYPE_KEY = "type"
        private const val PAYMENT_GATEWAY_TYPE = "PAYMENT_GATEWAY"
        private const val PARAMETERS_KEY = "parameters"
        private const val GATEWAY_KEY = "gateway"
        private const val RYFT_PAYMENT_GATEWAY = "ryft"
        private const val GATEWAY_MERCHANT_ID_KEY = "gatewayMerchantId"

        fun ryft(
            publicApiKey: RyftPublicApiKey
        ): TokenizationSpecification = TokenizationSpecification(
            gateway = RYFT_PAYMENT_GATEWAY,
            gatewayMerchantId = publicApiKey.value
        )
    }
}
