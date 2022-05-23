package com.ryftpay.android.ui.model.googlepay

import org.json.JSONArray
import org.json.JSONObject

internal data class LoadPaymentDataRequest(
    val merchantInfo: MerchantInfo,
    val billingAddressRequired: Boolean,
    val tokenizationSpecification: TokenizationSpecification,
    val transactionInfo: TransactionInfo,
    val emailRequired: Boolean
) {
    internal fun toApiV2RequestJson(
        baseApiRequest: BaseApiRequest
    ): JSONObject = JSONObject(
        baseApiRequest.toApiRequestJson().toString()
    ).apply {
        put(MerchantInfo.KEY, merchantInfo.toApiV2RequestJson())
        put(
            PaymentMethod.ALLOWED_PAYMENT_METHODS_KEY,
            JSONArray()
                .put(
                    CardPaymentMethod().toApiV2RequestJson(
                        billingAddressRequired,
                        tokenizationSpecification
                    )
                )
        )
        put(TransactionInfo.KEY, transactionInfo.toApiV2RequestJson())
        if (emailRequired) {
            put(EMAIL_REQUIRED_KEY, true)
        }
    }

    companion object {
        private const val EMAIL_REQUIRED_KEY = "emailRequired"
    }
}
